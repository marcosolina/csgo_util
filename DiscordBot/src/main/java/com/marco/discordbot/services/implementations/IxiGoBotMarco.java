package com.marco.discordbot.services.implementations;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import net.dv8tion.jda.api.requests.RestAction;

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
        if (jda == null || !botOnline) {
            try {
                LOGGER.debug("Starting the bot");
                // @formatter:off
                jda = JDABuilder.createDefault(botToken)
                        .enableIntents(GatewayIntent.GUILD_MEMBERS).addEventListeners(new IxiGoDiscordListenerMarco())
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
        checkIfBotIsOnline();
        LOGGER.debug("Stopping the bot");
        jda.shutdownNow();
        botOnline = false;
        return !botOnline;
    }

    @Override
    public List<DiscordUser> getMembers() throws MarcoException {
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
        checkIfBotIsOnline();
        LOGGER.debug("Moving everybody in the general channel");

        Guild guild = jda.getGuildById(dsProps.getServerId());
        VoiceChannel v = guild.getVoiceChannelById(dsProps.getVoiceChannels().getGeneral());

        // @formatter:off
        guild.loadMembers().get().parallelStream()
            .filter(m -> !m.getUser().isBot())
            .filter(m -> m.getVoiceState().inVoiceChannel())
            .forEach(m -> guild.moveVoiceMember(m, v).complete());
        // @formatter:on
        return true;
    }

    @Override
    public boolean moveDiscordUsersInTheAppropirateChannel() throws MarcoException {
        checkIfBotIsOnline();

        Guild guild = jda.getGuildById(dsProps.getServerId());
        Map<Long, Member> membersMap = new HashMap<>();
        // @formatter:off
        List<DiscordUser> onlineDiscordUsers = guild.loadMembers().get().parallelStream()
                .filter(m -> !m.getUser().isBot())
                .filter(m -> m.getVoiceState().inVoiceChannel()).map(m -> {
                    DiscordUser du = new DiscordUser();
                    du.setId(Long.toString(m.getIdLong()));
                    du.setName(m.getUser().getName());
                    membersMap.put(m.getIdLong(), m);
                    return du;
                }).collect(Collectors.toList());
        
        
        Map<String, Long> userMap = new HashMap<>();
        
        onlineDiscordUsers.stream()
            .map(du -> repo.findById(Long.parseLong(du.getId())))
            .forEach(entity -> userMap.put(entity.getSteamId(), entity.getDiscordId()));
        // @formatter:on

        if (terrorist != null && ct != null) {
            VoiceChannel teamRed = guild.getVoiceChannelById(dsProps.getVoiceChannels().getTerrorist());
            VoiceChannel teamBlue = guild.getVoiceChannelById(dsProps.getVoiceChannels().getCt());

            terrorist.getMembers().parallelStream().forEach(u -> {
                Long discordId = userMap.get(u.getSteamID());
                guild.moveVoiceMember(membersMap.get(discordId), teamRed).complete();
            });

            ct.getMembers().parallelStream().forEach(u -> {
                Long discordId = userMap.get(u.getSteamID());
                guild.moveVoiceMember(membersMap.get(discordId), teamBlue).complete();
            });

        }

        return false;
    }

    private void generateCsgoTeams(List<String> steamIds) {
        StringBuilder sb = new StringBuilder();
        steamIds.forEach(id -> sb.append("," + id));
        Map<String, String> queryParam = new HashMap<>();
        queryParam.put("usersIDs", sb.substring(1));

        terrorist = null;
        ct = null;

        try {
            URL url = new URL(demProps.getProtocol(), demProps.getHost(), demProps.getCreateTeams());
            ClientResponse clientResp = mnu.performGetRequest(url, Optional.empty(), Optional.of(queryParam));
            if (clientResp.statusCode() == HttpStatus.OK) {
                Teams teams = mnu.getBodyFromResponse(clientResp, Teams.class);
                if (teams.getTeams().size() == 2) {
                    terrorist = teams.getTeams().get(0);
                    ct = teams.getTeams().get(1);
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
            if(repo.findById(Long.parseLong(d.getId())) == null) {
                EntitySteamMap entity = new EntitySteamMap();
                entity.setDiscordId(Long.parseLong(d.getId()));
                entity.setDiscordName(d.getName());
                entity.setSteamId("");
                entity.setSteamName("");
                repo.persist(entity);
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
        checkIfBotIsOnline();

        Guild guild = jda.getGuildById(dsProps.getServerId());

        // @formatter:off
        List<DiscordUser> onlineDiscordUsers = guild.loadMembers().get().parallelStream()
                .filter(m -> !m.getUser().isBot())
                .filter(m -> m.getVoiceState().inVoiceChannel()).map(m -> {
                    DiscordUser du = new DiscordUser();
                    du.setId(Long.toString(m.getIdLong()));
                    du.setName(m.getUser().getName());
                    return du;
                }).collect(Collectors.toList());
        
        
        Map<String, Long> userMap = new HashMap<>();
        List<String> steamIds = new ArrayList<>();
        
        onlineDiscordUsers.stream()
            .map(du -> repo.findById(Long.parseLong(du.getId())))
            .forEach(entity -> {
                userMap.put(entity.getSteamId(), entity.getDiscordId());
                steamIds.add(entity.getSteamId());
            });
        // @formatter:on

        generateCsgoTeams(steamIds);
        if (terrorist != null) {

            StringBuilder sb = new StringBuilder();
            terrorist.getMembers().stream().forEach(m -> sb.append("," + m.getSteamID()));
            Map<String, String> queryParam = new HashMap<>();
            queryParam.put("terroristIDs", sb.substring(1));

            try {
                URL url = new URL(demProps.getProtocol(), demProps.getHost(), demProps.getMovePlayers());
                ClientResponse clientResp = mnu.performGetRequest(url, Optional.empty(), Optional.of(queryParam));
                return clientResp.statusCode() == HttpStatus.OK;
            } catch (MalformedURLException e) {
                LOGGER.error(e.getMessage());
                e.printStackTrace();
            }
        }

        return false;
    }
}
