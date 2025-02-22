package com.ixigo.discordbot.services.implementations;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import com.ixigo.demmanagercontract.models.rest.demdata.data.RestUsers;
import com.ixigo.discordbot.commands.errors.IxigoErrorCmd;
import com.ixigo.discordbot.config.properties.DiscordProps;
import com.ixigo.discordbot.constants.ErrorCodes;
import com.ixigo.discordbot.enums.TeamType;
import com.ixigo.discordbot.listeners.IxiGoDiscordListener;
import com.ixigo.discordbot.mappers.RepoMapper;
import com.ixigo.discordbot.models.repo.Bot_configDto;
import com.ixigo.discordbot.models.repo.Users_mapDto;
import com.ixigo.discordbot.models.svc.discord.SvcBotConfig;
import com.ixigo.discordbot.models.svc.discord.SvcDiscordUser;
import com.ixigo.discordbot.models.svc.discord.SvcPlayer;
import com.ixigo.discordbot.models.svc.discord.SvcSteamTeams;
import com.ixigo.discordbot.models.svc.discord.SvcSteamUser;
import com.ixigo.discordbot.repositories.interfaces.RepoBotConfig;
import com.ixigo.discordbot.repositories.interfaces.RepoUsersMap;
import com.ixigo.discordbot.services.interfaces.IxigoBot;
import com.ixigo.discordbot.services.interfaces.IxigoPlayersManagerService;
import com.ixigo.discordbot.services.interfaces.IxigoRconService;
import com.ixigo.enums.BotConfigKey;
import com.ixigo.library.errors.IxigoException;
import com.ixigo.library.mediators.web.interfaces.WebMediator;
import com.ixigo.library.messages.IxigoMessageResource;
import com.ixigo.library.validators.ValidationException;
import com.ixigo.models.rest.RestUser;
import com.ixigo.playersmanagercontract.models.rest.RestUserAvgScore;
import com.ixigo.serverhelper.models.rest.Cs2InputModel;
import com.netflix.servo.util.Strings;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
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
	private static final Logger _LOGGER = LoggerFactory.getLogger(IxigoBotImpl.class);

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
	@Autowired
	private RepoMapper mapper;
	@Autowired
	private WebMediator mediator;

	private static JDA jda;
	private boolean botOnline = false;

	@Override
	public synchronized Mono<Boolean> start() throws IxigoException {

		if (jda != null || botOnline) {
			_LOGGER.debug("Bot already running");
			return Mono.just(true);
		}

		return Mono.fromSupplier(() -> {
			try {
				_LOGGER.debug("Building the bot");
				// @formatter:off
				jda = JDABuilder.create(
						discordProps.getBottoken(),
						GatewayIntent.GUILD_MEMBERS,
						GatewayIntent.GUILD_MESSAGES,
						GatewayIntent.GUILD_VOICE_STATES,
						GatewayIntent.MESSAGE_CONTENT
						)
						.setChunkingFilter(ChunkingFilter.ALL)
						.setMemberCachePolicy(MemberCachePolicy.ALL)
						.addEventListeners(new IxiGoDiscordListener(this))
						.build();
				// @formatter:on
				jda.awaitReady();
				_LOGGER.debug("Starting the bot");
				botOnline = true;
			} catch (InterruptedException e) {
				_LOGGER.error(e.getMessage());
				e.printStackTrace();
				return false;
			}
			return true;
		}).subscribeOn(Schedulers.boundedElastic());
	}

	@Override
	public Mono<Boolean> stop() throws IxigoException {
		return checkIfBotIsOnlineOrStart(false).map(b -> {
			_LOGGER.debug("Stopping the bot");
			jda.shutdown();
			botOnline = false;
			return !botOnline;
		});
	}

	@Override
	public Flux<SvcDiscordUser> getDiscordUsers() throws IxigoException {
		var mono = getGuild().map(guild -> {
			// @formatter:off
	        return guild.getMembers().stream()
                    .filter(m -> !m.getUser().isBot())
                    .map(m -> {
                    	SvcDiscordUser du = new SvcDiscordUser();
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
					
					if(!actions.isEmpty()) {						
						RestAction.allOf(actions).queue();
					}
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
		var monoRepo = repoBotConfig.fingConfig(BotConfigKey.MATCHES_TO_CONSIDER_FOR_TEAM_CREATION);
		
		var monoResult = Mono.zip(monoRepo, monoRcon)
			.flatMap(tuple -> {
				Bot_configDto config = tuple.getT1();
				Integer numberOfMatches = Integer.parseInt(config.getConfig_val());
				
				Map<TeamType, List<RestUser>> teamsMap = tuple.getT2();
				if(_LOGGER.isDebugEnabled() && teamsMap != null) {
					_LOGGER.debug("Players retrieved from the server");
					_LOGGER.debug("- CT:");
					var list = teamsMap.get(TeamType.CT);
					if(list != null && !list.isEmpty()) {						
						list.forEach(l -> _LOGGER.debug(String.format("%s: %s", l.getUserName(), l.getSteamId())));
					}
					
					_LOGGER.debug("- Terrorists");
					list = teamsMap.get(TeamType.TERRORISTS);
					if(list != null && !list.isEmpty()) {
						list.forEach(l -> _LOGGER.debug(String.format("%s: %s", l.getUserName(), l.getSteamId())));
					}
				}				
				List<String> steamIds = new ArrayList<>();
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
		return getBotConfig(BotConfigKey.KICK_BOTS).map(config -> Boolean.parseBoolean(config.getConfigVal()))
				.flatMap(configActive -> {
					if (configActive) {
						return rconService.kickTheBots();
					}
					return Mono.just(configActive);
				});
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
	public Flux<SvcPlayer> getListOfMappedPlayers() throws IxigoException {
		// @formatter:off
		return getDiscordUsers()
			.flatMap(du -> {
				var userMap = new Users_mapDto();
				userMap.setDiscord_id(Long.parseLong(du.getId()));
				userMap.setDiscord_name(du.getName());
				userMap.setSteam_id("");
				userMap.setSteam_name("");
				
				return repoUsersMap.findById(Long.parseLong(du.getId()))
						.defaultIfEmpty(userMap);
			})
			.flatMap(userMap -> {
				if(userMap.getSteam_id().isEmpty()) {
					return repoUsersMap.insertOrUpdate(userMap).map(b -> userMap);
				}
				return Mono.just(userMap);
			})
			.map(userMap -> {
				SvcDiscordUser du = new SvcDiscordUser();
				du.setId(userMap.getDiscord_id().toString());
				du.setName(userMap.getDiscord_name());
				
				RestUsers ru = new RestUsers();
				ru.setSteam_id(userMap.getSteam_id());
				ru.setUser_name(userMap.getSteam_name());
				
				return new SvcPlayer(du, ru);
			})
			;
		// @formatter:on
	}

	@Override
	public Mono<Boolean> storePlayersDetails(List<SvcPlayer> players) throws IxigoException {
		// @formatter:off
		var monoList = players.stream().map(p -> {
			Users_mapDto dto = new Users_mapDto();
			dto.setDiscord_id(Long.parseLong(p.getDiscordDetails().getId()));
			dto.setDiscord_name(p.getDiscordDetails().getName());
			dto.setSteam_id(p.getSteamDetails().getSteam_id());
			dto.setSteam_name(p.getSteamDetails().getUser_name());
			return repoUsersMap.insertOrUpdate(dto);
		}).collect(Collectors.toList());
		
		return Mono.zip(monoList, arr -> {
			return Arrays.stream(arr)
				.map(el -> Boolean.class.cast(el))
				.allMatch(el -> el == true);
		});
		// @formatter:on
	}

	@Override
	public synchronized Mono<Boolean> updateBotConfig(SvcBotConfig config) throws IxigoException {
		return repoBotConfig.insertOtUpdate(mapper.raceFromSvcToDto(config));
	}

	@Override
	public Mono<SvcBotConfig> getBotConfig(BotConfigKey key) throws IxigoException {
		return repoBotConfig.fingConfig(key).map(mapper::raceFromDtoSvcToSvc);
	}

	@Override
	public void sendMessageToGeneralChat(String msg) {
		getGuild().subscribe(guidl -> {
			guidl.getTextChannelById(discordProps.getTextChannels().getGeneral()).sendMessage(msg).queue();
		});
	}

	@Override
	public void sendEmbedMessageToGeneralChat(MessageEmbed me) {
		getGuild().subscribe(guidl -> {
			guidl.getTextChannelById(discordProps.getTextChannels().getGeneral()).sendMessageEmbeds(me).queue();
		});
	}

	private void moveUsersToAppropriateVoiceChannel(List<Member> members) {
		// @formatter:off
		List<Member> membersInVoiceChat = members.stream()
			.filter(Objects::nonNull)
			.filter(m -> !m.getUser().isBot())
			.filter(m -> m.getVoiceState().inAudioChannel())
			.collect(Collectors.toList())
			;
		
		var discordUsersIds = membersInVoiceChat.stream().map(u -> u.getIdLong()).collect(Collectors.toList());
		var monoGuild = getGuild();
		var monoPlayersOnCsgoServer = rconService.getCurrentActivePlayersOnTheIxiGoServer();
		var monoListOfDiscordMappedUsers = repoUsersMap.findAllById(discordUsersIds).collectList();
		var monoGetMoveToChannelConfig = getBotConfig(BotConfigKey.MOVE_TO_VOICE_CHANNEL);
		
		Mono.zip(monoGuild, monoPlayersOnCsgoServer, monoListOfDiscordMappedUsers, monoGetMoveToChannelConfig)
		.doOnError(error -> {
			mediator.send(new IxigoErrorCmd(error.getMessage()));
		})
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
			
			if(!discordIdsToMove.isEmpty() && Boolean.parseBoolean(tuple.getT4().getConfigVal())) {
				_LOGGER.info(String.format("Getting voice channel by ID: %d", discordProps.getVoiceChannels().getTerrorist()));
				VoiceChannel vc = guild.getVoiceChannelById(discordProps.getVoiceChannels().getTerrorist());
				var actions = membersInVoiceChat.stream()
						.filter(m -> discordIdsToMove.contains(m.getIdLong()))
						.map(m -> moveMemberToVoiceChannel(guild, m, vc))
						.collect(Collectors.toList())
						;
				
				if(!actions.isEmpty()) {					
					RestAction.allOf(actions).queue();
				}
			}
		})
		;
		// @formatter:on
	}

	private Mono<Boolean> checkIfBotIsOnlineOrStart(boolean startIfOffline) throws ValidationException {
		if (!botOnline && startIfOffline) {
			return this.start();
		}

		return Mono.just(true);
	}

	private RestAction<Void> moveMemberToVoiceChannel(Guild guild, Member m, VoiceChannel v) {
		try {
			if (guild != null && m != null && v != null) {
				_LOGGER.debug(String.format("Moving %s", m.getUser().getName()));
				return guild.moveVoiceMember(m, v);
			} else {
				_LOGGER.error(String.format("guild null value: %b", guild == null));
				_LOGGER.error(String.format("member null value: %b", m == null));
				_LOGGER.error(String.format("voiche channel null value: %b", v == null));
				throw new IxigoException(HttpStatus.BAD_REQUEST, msgSource.getMessage(ErrorCodes.MOVE_TO_CHANNEL_FAIL),
						ErrorCodes.MOVE_TO_CHANNEL_FAIL);
			}
		} catch (Exception e) {
			_LOGGER.error(e.getMessage());
			throw new IxigoException(HttpStatus.INTERNAL_SERVER_ERROR,
					msgSource.getMessage(ErrorCodes.MOVE_TO_CHANNEL_FAIL), ErrorCodes.MOVE_TO_CHANNEL_FAIL);
		}
	}

	private Mono<Guild> getGuild() {
		return this.checkIfBotIsOnlineOrStart(true).map(b -> jda.getGuildById(discordProps.getServerId()));
	}

	@Override
	public Flux<SvcBotConfig> getBotConfigAll() throws IxigoException {
		return repoBotConfig.findAllConfig().map(mapper::raceFromDtoSvcToSvc);
	}

	@Override
	public Mono<Boolean> makeTeamsAndMoveToVoiceChannel() throws IxigoException {

		return repoBotConfig.fingConfig(BotConfigKey.MOVE_TO_VOICE_CHANNEL).flatMap(configActive -> {
			if (!Boolean.parseBoolean(configActive.getConfig_val())) {
				return Mono.just(true);
			}
			// @formatter:off
			var monoMapping = this.getListOfMappedPlayers().collectList();
			var monoRepo = repoBotConfig.fingConfig(BotConfigKey.MATCHES_TO_CONSIDER_FOR_TEAM_CREATION);
			var monoGuild = getGuild();
			
			return Mono.zip(monoGuild, monoMapping, monoRepo)
				.flatMap(tuple -> {
					var guild = tuple.getT1();
					var userMappings = tuple.getT2();
					var balanceConfig = tuple.getT3();
					
					Integer numberOfMatches = Integer.parseInt(balanceConfig.getConfig_val());
					
					var gtw = new GatewayTaskWrapper<List<Member>>(guild.loadMembers());
					
					return gtw.onSuccess(m -> {}).flatMap(members -> {
						List<Member> membersInVoiceChat = members.stream()
								.filter(Objects::nonNull)
								.filter(m -> !m.getUser().isBot())
								.filter(m -> m.getVoiceState().inAudioChannel())
								.collect(Collectors.toList())
								;
						var listOfDiscordIds = membersInVoiceChat.stream().map(Member::getId).collect(Collectors.toList());
						var steamIds = userMappings.stream()
								.filter(m -> listOfDiscordIds.contains(m.getDiscordDetails().getId()))
								.map(m -> m.getSteamDetails().getSteam_id())
								.collect(Collectors.toList());
						
						
						return Mono.zip(
								balanceService.generateBalancedTeams(steamIds, Optional.of(numberOfMatches), Optional.empty(), Optional.empty(), Optional.empty()),
								Mono.just(members));
					});
				})
				.map(tuple -> {
					var teams = tuple.getT1();
					var members = tuple.getT2();
					Function<RestUserAvgScore, SvcSteamUser> mapper = m -> {
						var svc = new SvcSteamUser();
						svc.setSteamId(m.getSteamID());
						svc.setUserName(m.getUserName());
						return svc;
					}; 
					var cts = teams.getTeams().get(1).getMembers().stream().map(mapper).collect(Collectors.toList());
					var terrorists = teams.getTeams().get(0).getMembers().stream().map(mapper).collect(Collectors.toList());
					
					var steamTeams = new SvcSteamTeams();
					steamTeams.setCt(cts);
					steamTeams.setTerrorists(terrorists);
					
					this.moveMemberToVoiceChannel(members, steamTeams);
					return true;
				})
				;
			// @formatter:on
		});
	}

	private void moveMemberToVoiceChannel(List<Member> members, SvcSteamTeams teams) {
		var terroristIds = teams.getTerrorists().stream().map(SvcSteamUser::getSteamId).collect(Collectors.toList());
		var ctIds = teams.getCt().stream().map(SvcSteamUser::getSteamId).collect(Collectors.toList());

		var terroristsDiscordIdsMono = repoUsersMap.findAllBySteamId(terroristIds).map(Users_mapDto::getDiscord_id)
				.collectList();
		var ctDiscordIdsMono = repoUsersMap.findAllBySteamId(ctIds).map(Users_mapDto::getDiscord_id).collectList();

		//// @formatter:off
		Mono.zip(terroristsDiscordIdsMono, ctDiscordIdsMono, getGuild())
			.subscribe(tuple -> {
				var tDiscIds = tuple.getT1();
				var ctDiscIds = tuple.getT2();
				var guild = tuple.getT3();
				
				VoiceChannel tVc = guild.getVoiceChannelById(discordProps.getVoiceChannels().getTerrorist());
				VoiceChannel ctVc = guild.getVoiceChannelById(discordProps.getVoiceChannels().getCt());
				
				List<Member> membersInVoiceChat = members.stream()
						.filter(Objects::nonNull)
						.filter(m -> !m.getUser().isBot())
						.filter(m -> m.getVoiceState().inAudioChannel())
						.collect(Collectors.toList())
						;
				
				var actions = membersInVoiceChat.stream()
						.filter(m -> tDiscIds.contains(m.getIdLong()))
						.map(m -> moveMemberToVoiceChannel(guild, m, tVc))
						.collect(Collectors.toList())
						;
				
				actions.addAll(membersInVoiceChat.stream()
						.filter(m -> ctDiscIds.contains(m.getIdLong()))
						.map(m -> moveMemberToVoiceChannel(guild, m, ctVc))
						.collect(Collectors.toList()))
						;
				
				if(!actions.isEmpty()) {					
					RestAction.allOf(actions).queue();
				}
			});
		// @formatter:on
	}

	@Override
	public Mono<Void> balanceMembersInVoiceChannel() throws IxigoException {

		var monoOptionEnabled = repoBotConfig.fingConfig(BotConfigKey.POST_TEAMS_ON_JOIN_LEFT_VOICE_CHANNEL);

		monoOptionEnabled.subscribe(optionEnabled -> {
			if (!Boolean.parseBoolean(optionEnabled.getConfig_val())) {
				return;
			}

			// @formatter:off
			var monoGuild = getGuild();
			var monoMapping = this.getListOfMappedPlayers().collectList();
			var monoRepo = repoBotConfig.fingConfig(BotConfigKey.MATCHES_TO_CONSIDER_FOR_TEAM_CREATION);
			
			Mono.zip(monoGuild, monoMapping, monoRepo)
			.subscribe(tuple -> {
				var guild = tuple.getT1();
				var mapping = tuple.getT2();
				
				Bot_configDto config = tuple.getT3();
				Integer numberOfMatches = Integer.parseInt(config.getConfig_val());
				
				guild.loadMembers().onSuccess(members -> {
					List<Member> membersInVoiceChat = members.stream()
							.filter(Objects::nonNull)
							.filter(m -> !m.getUser().isBot())
							.filter(m -> m.getVoiceState().inAudioChannel())
							.collect(Collectors.toList())
							;
					var listOfDiscordIds = membersInVoiceChat.stream().map(Member::getId).collect(Collectors.toList());
					var steamIds = mapping.stream()
							.filter(m -> listOfDiscordIds.contains(m.getDiscordDetails().getId()))
							.map(m -> m.getSteamDetails().getSteam_id())
							.collect(Collectors.toList());
					
					if(steamIds.size() < 3) {
						return;
					}
					
					balanceService.generateBalancedTeams(steamIds, Optional.of(numberOfMatches), Optional.empty(), Optional.empty(), Optional.empty())
					.subscribe(teams -> {
						var cts = teams.getTeams().get(1).getMembers();
						var terrorists = teams.getTeams().get(0).getMembers();
						
						StringBuilder ctMarkdownList = new StringBuilder();
						cts.forEach(ct -> {
							ctMarkdownList.append("- " + ct.getUserName() + "\n");
						});
						
						StringBuilder terroristsMarkdownList = new StringBuilder();
						terrorists.forEach(t -> {
							terroristsMarkdownList.append("- " + t.getUserName() + "\n");
						});
						
						MessageEmbed me = new EmbedBuilder()
								.setTitle("Balanced teams")
								.setColor(new Color(42, 255, 137))
								.addField("CTs", ctMarkdownList.toString(), true)
								.addField("          ", "", true)
								.addField("Terrorists", terroristsMarkdownList.toString(), true)
								.build();
						
						this.sendEmbedMessageToGeneralChat(me);
					});
				})
				;
			})
			;
			// @formatter:on
		});

		return Mono.empty();
	}

	@Override
	public Mono<Boolean> moveDiscordUsersInTheAppropriateChannelCs2() throws IxigoException {
		return this.makeTeamsAndMoveToVoiceChannel().map(r -> {
			_LOGGER.debug("Members moved to the appropriate voice channel");
			return r;
		});
	}

	@Override
	public Mono<Boolean> balanceTheTeamsCs2() throws IxigoException {
		_LOGGER.debug("Balancing CS2 teams");

		return repoBotConfig.fingConfig(BotConfigKey.AUTOBALANCE).flatMap(configActive -> {
			if (!Boolean.parseBoolean(configActive.getConfig_val())) {
				return Mono.just(true);
			}
			// @formatter:off
					var monoGuild = getGuild();
					var monoMapping = this.getListOfMappedPlayers().collectList();
					var monoRepo = repoBotConfig.fingConfig(BotConfigKey.MATCHES_TO_CONSIDER_FOR_TEAM_CREATION);
					
					return Mono.zip(monoGuild, monoMapping, monoRepo)
					.flatMap(tuple -> {
						var guild = tuple.getT1();
						var mapping = tuple.getT2();
						
						Bot_configDto config = tuple.getT3();
						Integer numberOfMatches = Integer.parseInt(config.getConfig_val());
						
						
						var wrappedTask = new GatewayTaskWrapper<List<Member>>(guild.loadMembers());
						return wrappedTask.onSuccess(m -> {})
							.flatMap(members -> {
								List<Member> membersInVoiceChat = members.stream()
										.filter(Objects::nonNull)
										.filter(m -> !m.getUser().isBot())
										.filter(m -> m.getVoiceState().inAudioChannel())
										.collect(Collectors.toList())
										;
								var listOfDiscordIds = membersInVoiceChat.stream().map(Member::getId).collect(Collectors.toList());
								var steamIds = mapping.stream()
										.filter(m -> listOfDiscordIds.contains(m.getDiscordDetails().getId()))
										.map(m -> m.getSteamDetails().getSteam_id())
										.collect(Collectors.toList());
								
								return balanceService.generateBalancedTeams(steamIds, Optional.of(numberOfMatches), Optional.empty(), Optional.empty(), Optional.empty());
							})
							.flatMap(teams -> {
								//var cts = teams.getTeams().get(1).getMembers();
								var terrorists = teams.getTeams().get(0).getMembers().stream().map(RestUserAvgScore::getSteamID).collect(Collectors.toList());
								
								_LOGGER.debug("Moving players on the CS2 server");
								return this.sendCs2RconCommand(String.format("ixigo_move %s", String.join(",", terrorists)));
							});
					})
					;
					// @formatter:on
		});

	}

	@Override
	public Mono<Boolean> kickTheBotsCs2() throws IxigoException {
		_LOGGER.trace("Kicking CS2 bots");
		
		return getBotConfig(BotConfigKey.KICK_BOTS)
				.map(config -> Boolean.parseBoolean(config.getConfigVal()))
				.flatMap(configActive -> {
					if (configActive) {
						_LOGGER.debug("Kicking CS2 bots");
						return this.sendCs2RconCommand("bot_kick");
					}
					return Mono.just(true);
				});
	}

	@Override
	public Mono<Boolean> restartCs2Match() throws IxigoException {
		_LOGGER.debug("Restarting CS2 match");
		return this.sendCs2RconCommand("mp_restartgame 5");
	}

	private Mono<Boolean> sendCs2RconCommand(String cmd) {
		var cs2Rcon = new Cs2InputModel();
		cs2Rcon.setCs2Input(cmd);
		return this.rconService.sendCs2Rcon(cs2Rcon);
	}
}
