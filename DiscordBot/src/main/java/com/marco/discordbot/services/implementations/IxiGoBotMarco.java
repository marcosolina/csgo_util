package com.marco.discordbot.services.implementations;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.security.auth.login.LoginException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;

import com.marco.discordbot.config.properties.DemParserProperties;
import com.marco.discordbot.config.properties.DiscordServerProps;
import com.marco.discordbot.enums.BotConfigKey;
import com.marco.discordbot.enums.TeamType;
import com.marco.discordbot.listeners.IxiGoDiscordListenerMarco;
import com.marco.discordbot.model.entities.EntityBotConfig;
import com.marco.discordbot.model.entities.EntitySteamMap;
import com.marco.discordbot.model.rest.BotConfig;
import com.marco.discordbot.model.rest.DiscordUser;
import com.marco.discordbot.model.rest.Player;
import com.marco.discordbot.model.rest.SteamUser;
import com.marco.discordbot.model.rest.roundparser.ActivePlayersResponse;
import com.marco.discordbot.model.rest.roundparser.Teams;
import com.marco.discordbot.model.rest.roundparser.User;
import com.marco.discordbot.repositories.interfaces.RepoEntityBotConfig;
import com.marco.discordbot.repositories.interfaces.RepoSteamMap;
import com.marco.discordbot.services.interfaces.IxiGoBot;
import com.marco.utils.MarcoException;
import com.marco.utils.MarcoException.ExceptionType;
import com.marco.utils.network.MarcoNetworkUtils;

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

    @Autowired
    private MessageSource msgSource;
    @Autowired
    private DiscordServerProps dsProps;
    @Autowired
    private DemParserProperties demProps;
    @Value("${discordbot.token}")
    private String botToken;
    @Autowired
    private MarcoNetworkUtils mnu;
    @Autowired
    private RepoSteamMap repo;
    @Autowired
    private RepoEntityBotConfig repoBotConfig;

    private boolean autoBalance = true;

    private void checkIfBotIsOnline() throws MarcoException {
        if (!botOnline) {
            throw new MarcoException("Warning",
                    msgSource.getMessage("DISCBOT00001", null, LocaleContextHolder.getLocale()), ExceptionType.warning);
        }
    }

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
                .map(m -> repo.findById(m.getIdLong())) // Retrieve the Steam ID of the Discord member
                .filter(Objects::nonNull) // Skip the not founded member
                .filter(entity -> entity.getSteamId() != null && !entity.getSteamId().isEmpty()) // I must have his steam ID
                .forEach(entity -> userMap.put(entity.getSteamId(), entity.getDiscordId()));// Store it in the map
        // @formatter:on
        return userMap;
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

            Map<TeamType, List<User>> inGamePlayers = getCurrentActivePlayersOnTheIxiGoServer();

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

    private Teams generateCsgoTeams() {
        Map<TeamType, List<User>> inGamePlayers = getCurrentActivePlayersOnTheIxiGoServer();

        if (inGamePlayers != null && inGamePlayers.size() == 2) {
            StringBuilder sb = new StringBuilder();
            inGamePlayers.get(TeamType.CT).stream().forEach(u -> sb.append("," + u.getSteamId()));
            inGamePlayers.get(TeamType.TERRORISTS).stream().forEach(u -> sb.append("," + u.getSteamId()));
            Map<String, String> queryParam = new HashMap<>();
            if (sb.length() == 0) {
                return null;
            }
            queryParam.put("usersIDs", sb.substring(1));
            try {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Calling the API to balance the teams");
                }
                /*
                 * Call the Round Parser service to balance the teams
                 */
                URL url = new URL(demProps.getProtocol(), demProps.getHost(),
                        demProps.getCreateTeams(getConfigRoundToConsiderForSplitTeam()));
                ClientResponse clientResp = mnu.performGetRequest(url, Optional.empty(), Optional.of(queryParam));
                if (clientResp.statusCode() == HttpStatus.OK) {
                    return mnu.getBodyFromResponse(clientResp, Teams.class);
                } else {
                    if (LOGGER.isErrorEnabled()) {
                        LOGGER.error(String.format("Error when calling: %s", url.toString()));
                    }
                }

            } catch (MalformedURLException e) {
                LOGGER.error(e.getMessage());
                e.printStackTrace();
            }
        }
        return null;
    }

    private int getConfigRoundToConsiderForSplitTeam() {
        EntityBotConfig config = repoBotConfig.fingConfig(BotConfigKey.ROUNDS_TO_CONSIDER_FOR_TEAM_CREATION);
        int roundsToConsider = 50;
        if (config != null) {
            try {
                roundsToConsider = Integer.parseInt(config.getConfigVal());
            } catch (Exception e) {
                LOGGER.error(e.getMessage());
            }
        }
        return roundsToConsider;
    }

    /**
     * It performs a REST call to retrieve the list of users currently playing on
     * the IxiGo Server
     * 
     * @return
     */
    private Map<TeamType, List<User>> getCurrentActivePlayersOnTheIxiGoServer() {
        try {
            URL url = new URL(demProps.getProtocol(), demProps.getHost(), demProps.getGetActivePlayers());
            ClientResponse clientResp = mnu.performGetRequest(url, Optional.empty(), Optional.empty());
            if (clientResp.statusCode() == HttpStatus.OK) {
                return mnu.getBodyFromResponse(clientResp, ActivePlayersResponse.class).getPlayers();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Player> getListOfMappedPlayers() throws MarcoException {
        // @formatter:off
        
        List<DiscordUser> discordUsers = getAllDiscordServerMembers();
        discordUsers.stream().forEach(d -> {
            EntitySteamMap e = repo.findById(Long.parseLong(d.getId()));
            if(e == null) { // New User, save it
                EntitySteamMap entity = new EntitySteamMap();
                entity.setDiscordId(Long.parseLong(d.getId()));
                entity.setDiscordName(d.getName());
                entity.setSteamId("");
                entity.setSteamName("");
                repo.persist(entity);
            }else if(!e.getDiscordName().equals(d.getName())){ // The Discord user name is changed, update the DB
                e.setDiscordName(d.getName());
                repo.persist(e);
            }
        });
        
        return repo.getAll().stream()
            .map(e -> new Player(
                    new DiscordUser(e.getDiscordId().toString(), e.getDiscordName()),
                    new SteamUser(e.getSteamId(), e.getSteamName())))
            .collect(Collectors.toList());
        // @formatter:on
    }

    @Override
    public boolean storePlayersDetails(List<Player> players) throws MarcoException {
        players.stream().forEach(p -> {
            EntitySteamMap entity = new EntitySteamMap();
            entity.setDiscordId(Long.parseLong(p.getDiscordDetails().getId()));
            entity.setDiscordName(p.getDiscordDetails().getName());
            entity.setSteamId(p.getSteamDetails().getSteamId());
            entity.setSteamName(p.getSteamDetails().getUserName());
            repo.persist(entity);
        });
        return true;
    }

    @Override
    public boolean balanceTheTeams() throws MarcoException {
        LOGGER.debug("IxiGoBotMarco.balanceTheTeams");
        checkIfBotIsOnline();

        Teams teams = generateCsgoTeams();
        if (teams != null) {

            /*
             * Sending the list of terrorist players
             */
            StringBuilder sb = new StringBuilder();
            teams.getTeams().get(0).getMembers().stream().forEach(m -> sb.append("," + m.getSteamID()));
            Map<String, String> queryParam = new HashMap<>();
            queryParam.put("terroristIDs", sb.substring(1));

            try {
                URL url = new URL(demProps.getProtocol(), demProps.getHost(), demProps.getMovePlayers());
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug(String.format("Setting terrorists: %s", queryParam.toString()));
                }

                ClientResponse clientResp = mnu.performGetRequest(url, Optional.empty(), Optional.of(queryParam));
                boolean ok = clientResp.statusCode() == HttpStatus.OK;
                if (ok) {
                    LOGGER.debug("CSGO Teams set");
                } else {
                    LOGGER.debug("CSGO Teams NOT set");
                }
                return ok;
            } catch (MalformedURLException e) {
                LOGGER.error(e.getMessage());
                e.printStackTrace();
            }
        }

        return false;
    }

    @Override
    public void setAutoBalance(boolean active) {
        this.autoBalance = active;
    }

    @Override
    public boolean isAutobalance() {
        return this.autoBalance;
    }

    @Override
    public boolean warmUpBalanceTeamApi() throws MarcoException {
        generateCsgoTeams();
        return true;
    }

    @Override
    public boolean restartIxiGoRound() throws MarcoException {
        try {
            URL url = new URL(demProps.getProtocol(), demProps.getHost(), demProps.getRestartRound());
            ClientResponse clientResp = mnu.performGetRequest(url, Optional.empty(), Optional.empty());
            boolean ok = clientResp.statusCode() == HttpStatus.OK;
            if (ok) {
                LOGGER.debug("Round restarted");
            } else {
                LOGGER.debug("Failed to restart the round");
            }
            return ok;
        } catch (MalformedURLException e) {
            LOGGER.error(e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateBotConfig(BotConfig config) throws MarcoException {
        EntityBotConfig entity = new EntityBotConfig();
        entity.setConfigKey(config.getConfigKey());
        entity.setConfigVal(config.getConfigVal());
        return repoBotConfig.saveConfig(entity);
    }

    @Override
    public BotConfig getBotConfig(BotConfigKey key) throws MarcoException {
        BotConfig config = null;
        EntityBotConfig entity = repoBotConfig.fingConfig(key);
        if(entity != null) {
            config = new BotConfig();
            config.setConfigKey(entity.getConfigKey());
            config.setConfigVal(entity.getConfigVal());
        }
        return config;
    }
}
