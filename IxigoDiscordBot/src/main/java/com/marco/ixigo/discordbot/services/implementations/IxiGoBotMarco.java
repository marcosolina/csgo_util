package com.marco.ixigo.discordbot.services.implementations;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.security.auth.login.LoginException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import com.marco.ixigo.discordbot.config.properties.DiscordProps;
import com.marco.ixigo.discordbot.enums.BotConfigKey;
import com.marco.ixigo.discordbot.enums.TeamType;
import com.marco.ixigo.discordbot.listeners.IxiGoDiscordListenerMarco;
import com.marco.ixigo.discordbot.model.Player;
import com.marco.ixigo.discordbot.model.demmanager.User;
import com.marco.ixigo.discordbot.model.discord.BotConfig;
import com.marco.ixigo.discordbot.model.discord.DiscordUser;
import com.marco.ixigo.discordbot.model.entities.EntityBotConfig;
import com.marco.ixigo.discordbot.model.entities.EntityUserMap;
import com.marco.ixigo.discordbot.model.playersmanager.Teams;
import com.marco.ixigo.discordbot.repositories.interfaces.RepoEntityBotConfig;
import com.marco.ixigo.discordbot.repositories.interfaces.RepoUsersMap;
import com.marco.ixigo.discordbot.services.interfaces.IxiGoBot;
import com.marco.ixigo.discordbot.services.interfaces.IxiGoGameServer;
import com.marco.utils.MarcoException;
import com.marco.utils.MarcoException.ExceptionType;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

public class IxiGoBotMarco implements IxiGoBot {
    private static final Logger LOGGER = LoggerFactory.getLogger(IxiGoBotMarco.class);
    
    private static JDA jda;
    private boolean botOnline = false;
    private boolean autoBalance = true;
    @Value("${com.marco.ixigo.discordbot.bottoken}")
    private String botToken;
    @Autowired
    private MessageSource msgSource;
    @Autowired
    private RepoUsersMap repoUsersMap;
    @Autowired
    private DiscordProps dsProps;
    @Autowired
    private IxiGoGameServer csgoService;
    @Autowired
    private RepoEntityBotConfig repoConfig;

    @Override
    public synchronized boolean start() throws MarcoException {
        LOGGER.debug("IxiGoBotMarco.start");
        if (jda == null || !botOnline) {
            try {
                LOGGER.debug("Starting the bot");
                // @formatter:off
                jda = JDABuilder.create(
                            botToken, 
                            GatewayIntent.GUILD_MEMBERS, 
                            GatewayIntent.GUILD_MESSAGES,
                            GatewayIntent.GUILD_VOICE_STATES)
                        .setChunkingFilter(ChunkingFilter.ALL)
                        .setMemberCachePolicy(MemberCachePolicy.ALL)
                        .addEventListeners(new IxiGoDiscordListenerMarco(this))
                        .build();
                // @formatter:on
                jda.awaitReady();
                LOGGER.debug("Bot Started");
                botOnline = true;
            } catch (LoginException | InterruptedException e) {
                LOGGER.error(e.getMessage());
                throw new MarcoException(e);
            }

        }
        return botOnline;
    }

    @Override
    public synchronized boolean stop() throws MarcoException {
        LOGGER.debug("IxiGoBotMarco.stop");
        checkIfBotIsOnline();
        LOGGER.debug("Stopping the bot");
        jda.shutdownNow();
        botOnline = false;
        return !botOnline;
    }

    @Override
    public List<DiscordUser> getAllDiscordServerMembers() throws MarcoException {
        LOGGER.debug("IxiGoBotMarco.getMembers");
        checkIfBotIsOnline();

        Guild guild = jda.getGuildById(dsProps.getServerId());

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
    }

    @Override
    public boolean moveAllMembersIntoGeneralChannel() throws MarcoException {
        LOGGER.debug("IxiGoBotMarco.moveAllMembersIntoGeneralChannel");
        checkIfBotIsOnline();

        Guild guild = jda.getGuildById(dsProps.getServerId());
        VoiceChannel v = guild.getVoiceChannelById(dsProps.getVoiceChannels().getGeneral());

        // @formatter:off
        guild.loadMembers().get().stream()
            .filter(Objects::nonNull)
            .filter(m -> !m.getUser().isBot())
            .filter(m -> m.getVoiceState().inVoiceChannel())
            .forEach(m -> moveMemberToVoiceChannel(guild, m, v));
        // @formatter:on
        return true;
    }

    @Override
    public boolean moveDiscordUsersInTheAppropirateChannel() throws MarcoException {
        LOGGER.debug("IxiGoBotMarco.moveDiscordUsersInTheAppropirateChannel");
        checkIfBotIsOnline();

        Map<String, Long> userMap = getUsersInAVoiceChannel();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(String.format("There are %d members online", userMap.size()));
        }

        /*
         * Loop through the teams, retrieve the cached discord ID and move the user into
         * the appropriate channel
         */
        Guild guild = jda.getGuildById(dsProps.getServerId());
        boolean status = false;
        if (userMap.size() > 0) {

            Map<TeamType, List<User>> inGamePlayers = csgoService.getCurrentActivePlayersOnTheIxiGoServer();

            if (inGamePlayers != null && inGamePlayers.get(TeamType.TERRORISTS) != null) {
                VoiceChannel teamRed = guild.getVoiceChannelById(dsProps.getVoiceChannels().getTerrorist());

                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug(String.format("Moving to team: %s", teamRed.getName()));
                }

                inGamePlayers.get(TeamType.TERRORISTS).stream().forEach(u -> {
                    Long discordId = userMap.get(u.getSteamId());
                    if (discordId != null) {
                        this.moveMemberToVoiceChannel(guild, guild.getMemberById(discordId), teamRed);
                    } else {
                        LOGGER.error("Member not in cache " + u.getSteamId());
                    }
                });
                status = true;
            }
        }
        LOGGER.debug("Players moved to the voice channels");

        return status;
    }

    @Override
    public boolean balanceTheTeams() throws MarcoException {
        Teams teams = csgoService.generateCsgoTeams();
        return csgoService.movePlayersToAppropriateTeam(teams);
    }

    @Override
    public boolean restartIxiGoRound() throws MarcoException {
        return csgoService.restartIxiGoRound();
    }

    @Override
    public boolean warmUpBalanceTeamApi() throws MarcoException {
        csgoService.generateCsgoTeams();
        return true;
    }

    @Override
    public void setAutoBalance(boolean active) {
        autoBalance = active;
    }

    @Override
    public boolean isAutobalance() {
        return autoBalance;
    }

    @Override
    public List<Player> getListOfMappedPlayers() throws MarcoException {
// @formatter:off
        
        List<DiscordUser> discordUsers = getAllDiscordServerMembers();
        discordUsers.stream().forEach(d -> {
            EntityUserMap e = repoUsersMap.findById(Long.parseLong(d.getId()));
            if(e == null) { // New User, save it
                EntityUserMap entity = new EntityUserMap();
                entity.setDiscordId(Long.parseLong(d.getId()));
                entity.setDiscordName(d.getName());
                entity.setSteamId("");
                entity.setSteamName("");
                repoUsersMap.persist(entity);
            }else if(!e.getDiscordName().equals(d.getName())){ // The Discord user name is changed, update the DB
                e.setDiscordName(d.getName());
                repoUsersMap.persist(e);
            }
        });
        
        return repoUsersMap.getAll().stream()
            .map(e -> new Player(
                    new DiscordUser(e.getDiscordId().toString(), e.getDiscordName()),
                    new User(e.getSteamId(), e.getSteamName())))
            .collect(Collectors.toList());
        // @formatter:on
    }

    @Override
    public boolean storePlayersDetails(List<Player> players) throws MarcoException {
        players.stream().forEach(p -> {
            EntityUserMap entity = new EntityUserMap();
            entity.setDiscordId(Long.parseLong(p.getDiscordDetails().getId()));
            entity.setDiscordName(p.getDiscordDetails().getName());
            entity.setSteamId(p.getSteamDetails().getSteamId());
            entity.setSteamName(p.getSteamDetails().getUserName());
            repoUsersMap.persist(entity);
        });
        return true;
    }

    @Override
    public boolean updateBotConfig(BotConfig config) throws MarcoException {
        EntityBotConfig bc = new EntityBotConfig();
        bc.setConfigKey(config.getConfigKey());
        bc.setConfigVal(config.getConfigVal());
        return repoConfig.saveConfig(bc);
    }

    @Override
    public BotConfig getBotConfig(BotConfigKey key) throws MarcoException {
        EntityBotConfig bc = repoConfig.fingConfig(key);
        if(bc != null) {
            return new BotConfig(bc.getConfigKey(), bc.getConfigVal());
        }
        return null;
    }
    
    /**
     * It returns a map which contains the Users which are in a voice channel
     * <ul>
     * <li><b>KEY:</b> Steam ID</li>
     * <li><b>VALUE:</b> Discord ID</li>
     * </ul>
     * 
     * @return
     */
    private Map<String, Long> getUsersInAVoiceChannel() {
        Map<String, Long> userMap = new HashMap<>();

        Guild guild = jda.getGuildById(dsProps.getServerId());
        // @formatter:off
        guild.loadMembers().get().stream()
                .filter(Objects::nonNull)
                .filter(m -> !m.getUser().isBot()) // Skip the bots
                .filter(m -> m.getVoiceState().inVoiceChannel()) // The member must be in any voice channel
                .map(m -> repoUsersMap.findById(m.getIdLong())) // Retrieve the Steam ID of the Discord member
                .filter(Objects::nonNull) // Skip the not founded member
                .filter(entity -> entity.getSteamId() != null && !entity.getSteamId().isEmpty()) // I must have his steam ID
                .forEach(entity -> userMap.put(entity.getSteamId(), entity.getDiscordId()));// Store it in the map
        // @formatter:on
        return userMap;
    }
    
    private void checkIfBotIsOnline() throws MarcoException {
        if (!botOnline) {
            throw new MarcoException("Warning",
                    msgSource.getMessage("DISCBOT00001", null, LocaleContextHolder.getLocale()), ExceptionType.warning);
        }
    }
    
    private void moveMemberToVoiceChannel(Guild guild, Member m, VoiceChannel v) {
        try {
            if (guild != null && m != null && v != null) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug(String.format("Moving %s", m.getUser().getName()));
                }
                guild.moveVoiceMember(m, v).queue();
            } else {
                LOGGER.error("Either the Guild, m or v are null");
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }
}
