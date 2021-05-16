package com.marco.discordbot.services.implementations;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
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
import com.marco.discordbot.listeners.IxiGoDiscordListenerMarco;
import com.marco.discordbot.model.entities.EntitySteamMap;
import com.marco.discordbot.model.rest.DiscordUser;
import com.marco.discordbot.model.rest.Player;
import com.marco.discordbot.model.rest.SteamUser;
import com.marco.discordbot.model.rest.roundparser.GeneratedTeams;
import com.marco.discordbot.model.rest.roundparser.Team;
import com.marco.discordbot.model.rest.roundparser.Teams;
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

    private boolean autoBalance = false;

    private static Team terrorist = null;
    private static Team ct = null;

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
                jda = JDABuilder.createDefault(botToken)
                        //.setChunkingFilter(ChunkingFilter.NONE)
                        //.setMemberCachePolicy(MemberCachePolicy.NONE)
                        .enableIntents(GatewayIntent.GUILD_MEMBERS)
                        .addEventListeners(new IxiGoDiscordListenerMarco(this, this.dsProps))
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
    public List<DiscordUser> getMembers() throws MarcoException {
        LOGGER.debug("IxiGoBotMarco.getMembers");
        checkIfBotIsOnline();

        Guild guild = jda.getGuildById(dsProps.getServerId());

        // @formatter:off
        return guild.loadMembers().get().parallelStream()
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

        Guild guild = jda.getGuildById(dsProps.getServerId());
        Map<Long, Member> membersMap = new HashMap<>();
        // @formatter:off
        /*
         * Get the definition of the online users in Discord.
         * These users are already in a vaoice channe
         */
        List<DiscordUser> onlineDiscordUsers = guild.loadMembers().get().parallelStream()
                .filter(m -> !m.getUser().isBot())
                .filter(m -> m.getVoiceState().inVoiceChannel()).map(m -> {
                    DiscordUser du = new DiscordUser();
                    du.setId(Long.toString(m.getIdLong()));
                    du.setName(m.getUser().getName());
                    membersMap.put(m.getIdLong(), m);
                    return du;
                }).collect(Collectors.toList());
        
        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug(String.format("There are %d members online", onlineDiscordUsers.size()));
        }
        
        /*
         * Caching the mapping between steam user
         * id and discord user id
         * KEY -> Steam ID
         * VALUE -> Discord ID 
         */
        Map<String, Long> userMap = new HashMap<>();
        onlineDiscordUsers.parallelStream()
            .map(du -> repo.findById(Long.parseLong(du.getId())))
            .filter(Objects::nonNull)
            .filter(entity -> entity.getSteamId() != null && !entity.getSteamId().isEmpty())
            .forEach(entity -> userMap.put(entity.getSteamId(), entity.getDiscordId()));
        // @formatter:on

        /*
         * Loop through the teams, retrieve the cached discord ID and move the user into
         * the appropriate channel
         */
        boolean status = false;
        if(userMap.size() > 0) {
            if (terrorist != null && terrorist.getMembers() != null && !terrorist.getMembers().isEmpty()) {
                VoiceChannel teamRed = guild.getVoiceChannelById(dsProps.getVoiceChannels().getTerrorist());
                
                if(LOGGER.isDebugEnabled()) {
                    LOGGER.debug(String.format("Moving to team: %s", teamRed.getName()));
                }
                
                terrorist.getMembers().stream().forEach(u -> {
                    Long discordId = userMap.get(u.getSteamID());
                    this.moveMemberToVoiceChannel(guild, membersMap.get(discordId), teamRed);
                });
                status = true;
            }
            
            if (ct != null && ct.getMembers() != null && !ct.getMembers().isEmpty()) {
                VoiceChannel teamBlue = guild.getVoiceChannelById(dsProps.getVoiceChannels().getCt());
                if(LOGGER.isDebugEnabled()) {
                    LOGGER.debug(String.format("Moving to team: %s", teamBlue.getName()));
                }
                
                ct.getMembers().stream().forEach(u -> {
                    Long discordId = userMap.get(u.getSteamID());
                    this.moveMemberToVoiceChannel(guild, membersMap.get(discordId), teamBlue);
                });
                status = true;
            }
        }
        LOGGER.debug("Players moved to the voice channels");

        return status;
    }
    
    private void moveMemberToVoiceChannel(Guild guild, Member m, VoiceChannel v) {
        try {
            if(guild != null && m != null && v != null) {
                if(LOGGER.isDebugEnabled()) {
                    LOGGER.debug(String.format("Moving %s", m.getUser().getName()));
                }
                guild.moveVoiceMember(m, v).queue();
            }else {
                LOGGER.error("Either the Guild, m or v are null");
            }
        } catch(Exception e) {
            LOGGER.error(e.getMessage());
        }
    }

    private void generateCsgoTeams(List<String> steamIds) {
        terrorist = null;
        ct = null;

        if (steamIds == null || steamIds.isEmpty()) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        steamIds.forEach(id -> sb.append("," + id));
        Map<String, String> queryParam = new HashMap<>();
        queryParam.put("usersIDs", sb.substring(1));

        try {
            if(LOGGER.isDebugEnabled()) {
                LOGGER.debug("Calling the API to balance the teams");
            }
            /*
             * Call the Round Parser service to balance the teams
             */
            URL url = new URL(demProps.getProtocol(), demProps.getHost(), demProps.getCreateTeams());
            ClientResponse clientResp = mnu.performGetRequest(url, Optional.empty(), Optional.of(queryParam));
            if (clientResp.statusCode() == HttpStatus.OK) {
                Teams teams = mnu.getBodyFromResponse(clientResp, Teams.class);
                
                if(LOGGER.isDebugEnabled()) {
                    LOGGER.debug(teams.toString());
                }
                
                switch (teams.getTeams().size()) {
                case 2:
                    ct = teams.getTeams().get(1);
                    // there is no break on purpose
                case 1:
                    terrorist = teams.getTeams().get(0);
                    break;
                default:
                    break;
                }
            }else {
                if(LOGGER.isErrorEnabled()) {
                    LOGGER.error(String.format("Error when calling: %s", url.toString()));
                }
            }
            
        } catch (MalformedURLException e) {
            LOGGER.error(e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public List<Player> getListOfMappedPlayers() throws MarcoException {
        // @formatter:off
        
        List<DiscordUser> discordUsers = getMembers();
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

        Guild guild = jda.getGuildById(dsProps.getServerId());

        /*
         * Get the list of discord user in a voice channel
         */
        // @formatter:off
        List<DiscordUser> onlineDiscordUsers = guild.loadMembers().get().parallelStream()
                .filter(m -> !m.getUser().isBot())
                .filter(m -> m.getVoiceState().inVoiceChannel()).map(m -> {
                    DiscordUser du = new DiscordUser();
                    du.setId(Long.toString(m.getIdLong()));
                    du.setName(m.getUser().getName());
                    return du;
                }).collect(Collectors.toList());
        
        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug(String.format("There are %d members online", onlineDiscordUsers.size()));
        }
        
        List<String> steamIds = new ArrayList<>();
        
        onlineDiscordUsers.stream()
            .map(du -> repo.findById(Long.parseLong(du.getId())))
            .filter(Objects::nonNull)
            .filter(entity -> entity.getSteamId() != null && !entity.getSteamId().isEmpty())
            .forEach(entity -> steamIds.add(entity.getSteamId()));
        // @formatter:on

        generateCsgoTeams(steamIds);
        if (terrorist != null) {

            /*
             * Sending the list of terrorist players
             */
            StringBuilder sb = new StringBuilder();
            terrorist.getMembers().stream().forEach(m -> sb.append("," + m.getSteamID()));
            Map<String, String> queryParam = new HashMap<>();
            queryParam.put("terroristIDs", sb.substring(1));

            try {
                URL url = new URL(demProps.getProtocol(), demProps.getHost(), demProps.getMovePlayers());
                if(LOGGER.isDebugEnabled()) {
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
    public GeneratedTeams getCurrentTeams() throws MarcoException {

        checkIfBotIsOnline();

        Guild guild = jda.getGuildById(dsProps.getServerId());
        Map<Long, Member> membersMap = new HashMap<>();
        // @formatter:off
        /*
         * Get the definition of the online users in Discord.
         * These users are already in a vaoice channe
         */
        List<DiscordUser> onlineDiscordUsers = guild.loadMembers().get().parallelStream()
                .filter(m -> !m.getUser().isBot())
                .filter(m -> m.getVoiceState().inVoiceChannel()).map(m -> {
                    DiscordUser du = new DiscordUser();
                    du.setId(Long.toString(m.getIdLong()));
                    du.setName(m.getUser().getName());
                    membersMap.put(m.getIdLong(), m);
                    return du;
                }).collect(Collectors.toList());
        
        /*
         * Caching the mapping between steam user
         * id and discord user id
         * KEY -> Steam ID
         * VALUE -> Discord ID 
         */
        Map<String, Long> userMap = new HashMap<>();
        onlineDiscordUsers.parallelStream()
            .map(du -> repo.findById(Long.parseLong(du.getId())))
            .filter(Objects::nonNull)
            .filter(entity -> entity.getSteamId() != null && !entity.getSteamId().isEmpty())
            .forEach(entity -> userMap.put(entity.getSteamId(), entity.getDiscordId()));

        GeneratedTeams t = new GeneratedTeams();
        if (terrorist != null) {
            t.setTerrorist(terrorist.getMembers().parallelStream().map(u -> {
                Long discordId = userMap.get(u.getSteamID());
                Member m = membersMap.get(discordId);
                DiscordUser du = new DiscordUser();
                du.setId(Long.toString(m.getIdLong()));
                du.setName(m.getUser().getName());
                return du;
            }).collect(Collectors.toList()));
        }
        
        if (ct != null) {
            t.setCt(ct.getMembers().parallelStream().map(u -> {
                Long discordId = userMap.get(u.getSteamID());
                Member m = membersMap.get(discordId);
                DiscordUser du = new DiscordUser();
                du.setId(Long.toString(m.getIdLong()));
                du.setName(m.getUser().getName());
                return du;
            }).collect(Collectors.toList()));

        }
        // @formatter:on
        return t;
    }
}
