package com.ixigo.discordbot.services.implementations;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import com.ixigo.demmanagercontract.models.rest.demdata.RestUser;
import com.ixigo.discordbot.config.properties.DiscordProps;
import com.ixigo.discordbot.constants.ErrorCodes;
import com.ixigo.discordbot.enums.BotConfigKey;
import com.ixigo.discordbot.enums.TeamType;
import com.ixigo.discordbot.listeners.IxiGoDiscordListener;
import com.ixigo.discordbot.models.repo.Bot_configDto;
import com.ixigo.discordbot.models.repo.Users_mapDto;
import com.ixigo.discordbot.models.svc.discord.DiscordUser;
import com.ixigo.discordbot.models.svc.discord.SvcBotConfig;
import com.ixigo.discordbot.models.svc.discord.SvcPlayer;
import com.ixigo.discordbot.repositories.interfaces.RepoBotConfig;
import com.ixigo.discordbot.repositories.interfaces.RepoUsersMap;
import com.ixigo.discordbot.services.interfaces.IxigoBot;
import com.ixigo.discordbot.services.interfaces.IxigoPlayersManagerService;
import com.ixigo.discordbot.services.interfaces.IxigoRconService;
import com.ixigo.library.errors.IxigoException;
import com.ixigo.library.messages.IxigoMessageResource;
import com.netflix.servo.util.Strings;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * 
 * @see <a href=
 *      "https://itnext.io/how-to-make-legacy-code-reactive-2debcb3d0a40">Nice
 *      article to make blocming code in non blocking code</a>
 * @author Marco
 *
 */
public class IxigoBotImpl implements IxigoBot {
	private static final Logger LOGGER = LoggerFactory.getLogger(IxigoBotImpl.class);

	@Autowired
	private IxigoMessageResource msgSource;
	@Autowired
	private DiscordProps discordProps;
	@Autowired
	private RepoUsersMap repoUsersMap;
	@Autowired
	private RepoBotConfig repoBotConfig;
	@Autowired
	private IxigoRconService rconService;
	@Autowired
	private IxigoPlayersManagerService balanceService;

	private static JDA jda;
	private boolean botOnline = false;
	private boolean autoBalance = true;
	private boolean kickBots = true;

	@Override
	public synchronized Mono<Boolean> start() throws IxigoException {

		if (jda != null || botOnline) {
			LOGGER.debug("Bot already running");
			return Mono.just(true);
		}

		return Mono.fromSupplier(() -> {
			try {
				// @formatter:off
				jda = JDABuilder.create(
						discordProps.getBottoken(),
						GatewayIntent.GUILD_MEMBERS,
						GatewayIntent.GUILD_MESSAGES,
						GatewayIntent.GUILD_VOICE_STATES
						)
						.setChunkingFilter(ChunkingFilter.ALL)
						.setMemberCachePolicy(MemberCachePolicy.ALL)
						.addEventListeners(new IxiGoDiscordListener(this))
						.build();
				// @formatter:on
				jda.awaitReady();
				LOGGER.debug("Starting the bot");
				botOnline = true;
			} catch (InterruptedException e) {
				LOGGER.error(e.getMessage());
				e.printStackTrace();
				return false;
			}
			return true;
		}).subscribeOn(Schedulers.boundedElastic());
	}

	@Override
	public Mono<Boolean> stop() throws IxigoException {
		return Mono.fromSupplier(() -> {
			checkIfBotIsOnline();
			LOGGER.debug("Stopping the bot");
			jda.shutdown();
			botOnline = false;
			return !botOnline;
		}).subscribeOn(Schedulers.boundedElastic());
	}

	@Override
	public Flux<DiscordUser> getDiscordOnlineUsers() throws IxigoException {
		var mono = getGuild().map(guild -> {
			// @formatter:off
	        return guild.loadMembers().get().stream()
                    .filter(m -> !m.getUser().isBot())
                    .map(m -> {
                    	DiscordUser du = new DiscordUser();
                        du.setId(Long.toString(m.getIdLong()));
                        du.setName(m.getUser().getName());
                        return du;
                    }).collect(Collectors.toList());
	        // @formatter:on
		});

		return mono.flatMapMany(Flux::fromIterable);
	}

	@Override
	public void moveAllMembersIntoGeneralChannel() throws IxigoException {
		// @formatter:off
		getGuild()
		.subscribe(guild -> {
			VoiceChannel vc = guild.getVoiceChannelById(discordProps.getVoiceChannels().getGeneral());
			guild.loadMembers()
				.onSuccess(list -> {
					var actions = list.stream()
						.filter(Objects::nonNull)
						.filter(m -> !m.getUser().isBot())
						.filter(m -> m.getVoiceState().inAudioChannel())
						.map(m -> moveMemberToVoiceChannel(guild, m, vc))
						.collect(Collectors.toList())
						;
					RestAction.allOf(actions).queue();
				});
		});
		;
		// @formatter:on
	}

	@Override
	public void moveDiscordUsersInTheAppropriateChannel() throws IxigoException {
		// @formatter:off
		getGuild()
		.subscribe(guild -> {
			guild.loadMembers()
			.onSuccess(this::moveUsersToAppropriateVoiceChannel)
			;
		})
		;
		// @formatter:on
	}

	@Override
	public Mono<Boolean> balanceTheTeams() throws IxigoException {
		// @formatter:off
		var monoRcon = rconService.getCurrentActivePlayersOnTheIxiGoServer();
		var monoRepo = repoBotConfig.fingConfig(BotConfigKey.ROUNDS_TO_CONSIDER_FOR_TEAM_CREATION);
		
		var monoResult = Mono.zip(monoRepo, monoRcon)
			.flatMap(tuple -> {
				Bot_configDto config = tuple.getT1();
				Integer numberOfMatches = Integer.parseInt(config.getConfig_val());
				
				List<String> steamIds = new ArrayList<>();
				Map<TeamType, List<RestUser>> teamsMap = tuple.getT2();
				teamsMap.forEach((k,v) -> {
					v.stream().map(u -> u.getSteamId()).forEach(steamIds::add);
				});
				return balanceService.generateBalancedTeams(steamIds, Optional.of(numberOfMatches), Optional.empty(), Optional.empty(), Optional.empty());
			})
			.flatMap(rconService::movePlayersToAppropriateTeam)
		;
		return monoResult;
		// @formatter:on
	}

	@Override
	public Mono<Boolean> kickTheBots() throws IxigoException {
		return kickBots ? rconService.kickTheBots() : Mono.just(false);
	}

	@Override
	public Mono<Boolean> restartCsgoMatch() throws IxigoException {
		return rconService.restartIxiGoMatch();
	}

	@Override
	public Mono<Boolean> warmUpBalanceTeamApi() throws IxigoException {
		return rconService.getCurrentActivePlayersOnTheIxiGoServer().map(map -> map != null);
	}

	@Override
	public Mono<Void> setAutoBalance(boolean active) {
		this.autoBalance = active;
		return Mono.empty();
	}

	@Override
	public Mono<Boolean> isAutobalance() {
		return Mono.just(autoBalance);
	}

	@Override
	public Mono<Boolean> isKickBots() {
		return Mono.just(kickBots);
	}

	@Override
	public Mono<Void> setKickBots(boolean active) {
		kickBots = active;
		return Mono.empty();
	}

	@Override
	public Flux<SvcPlayer> getListOfMappedPlayers() throws IxigoException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Mono<Boolean> storePlayersDetails(List<SvcPlayer> players) throws IxigoException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Mono<Boolean> updateBotConfig(SvcBotConfig config) throws IxigoException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Mono<SvcBotConfig> getBotConfig(BotConfigKey key) throws IxigoException {
		// TODO Auto-generated method stub
		return null;
	}

	private void moveUsersToAppropriateVoiceChannel(List<Member> members) {
		// @formatter:off
		List<Member> membersInVoiceChat = members.stream()
			.filter(Objects::nonNull)
			.filter(m -> !m.getUser().isBot())
			.filter(m -> m.getVoiceState().inAudioChannel())
			.collect(Collectors.toList())
			;
		
		var usersTsearchMappingFor = membersInVoiceChat.stream().map(u -> u.getIdLong()).collect(Collectors.toList());
		
		var monoGuild = getGuild();
		var monoPlayersOnCsgoServer = rconService.getCurrentActivePlayersOnTheIxiGoServer();
		var monoListOfDiscordMappedUsers = repoUsersMap.findAllById(usersTsearchMappingFor).collectList();
		
		Mono.zip(monoGuild, monoPlayersOnCsgoServer, monoListOfDiscordMappedUsers)
		.subscribe(tuple -> {
			Guild guild = tuple.getT1();
			Map<TeamType, List<RestUser>> mapActivePlayers = tuple.getT2();
			List<Users_mapDto> discordMappedUsers = tuple.getT3();
			
			var terroristCurrentlyPlaying = mapActivePlayers.get(TeamType.TERRORISTS)
					.stream()
					.map(RestUser::getSteamId)
					.collect(Collectors.toList());
			
			var discordIdsToMove = discordMappedUsers.stream()
				.filter(u -> !Strings.isNullOrEmpty(u.getSteam_id()))
				.filter(u -> terroristCurrentlyPlaying.contains(u.getSteam_id()))
				.map(u -> u.getDiscord_id())
				.collect(Collectors.toList())
			;
			
			if(!discordIdsToMove.isEmpty()) {
				VoiceChannel vc = guild.getVoiceChannelById(discordProps.getVoiceChannels().getTerrorist());
				var actions = membersInVoiceChat.stream()
						.filter(m -> discordIdsToMove.contains(m.getIdLong()))
						.map(m -> moveMemberToVoiceChannel(guild, m, vc))
						.collect(Collectors.toList())
						;
				
				RestAction.allOf(actions).queue();
			}
		})
		;
		// @formatter:on
	}

	private void checkIfBotIsOnline() throws IxigoException {
		if (!botOnline) {
			throw new IxigoException(HttpStatus.BAD_REQUEST, msgSource.getMessage(ErrorCodes.BOT_OFFLINE), ErrorCodes.BOT_OFFLINE);
		}
	}

	private RestAction<Void> moveMemberToVoiceChannel(Guild guild, Member m, VoiceChannel v) {
		try {
			if (guild != null && m != null && v != null) {
				LOGGER.debug(String.format("Moving %s", m.getUser().getName()));
				return guild.moveVoiceMember(m, v);
			} else {
				throw new IxigoException(HttpStatus.BAD_REQUEST, msgSource.getMessage(ErrorCodes.MOVE_TO_CHANNEL_FAIL), ErrorCodes.MOVE_TO_CHANNEL_FAIL);
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw new IxigoException(HttpStatus.INTERNAL_SERVER_ERROR, msgSource.getMessage(ErrorCodes.MOVE_TO_CHANNEL_FAIL), ErrorCodes.MOVE_TO_CHANNEL_FAIL);
		}
	}

	private Mono<Guild> getGuild() {
		return Mono.fromSupplier(() -> {
			checkIfBotIsOnline();

			return jda.getGuildById(discordProps.getServerId());
		}).subscribeOn(Schedulers.boundedElastic());
	}
}
