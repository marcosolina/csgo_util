package com.marco.discordbot.services.implementations;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.security.auth.login.LoginException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestBodySpec;
import org.springframework.web.util.UriBuilder;

import com.marco.discordbot.config.properties.DiscordServerProps;
import com.marco.discordbot.listeners.IxiGoDiscordListenerMarco;
import com.marco.discordbot.model.entities.EntitySteamMap;
import com.marco.discordbot.model.rest.DiscordUser;
import com.marco.discordbot.model.rest.Player;
import com.marco.discordbot.model.rest.SteamUser;
import com.marco.discordbot.repositories.interfaces.RepoSteamMap;
import com.marco.discordbot.services.interfaces.IxiGoBot;
import com.marco.utils.MarcoException;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
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
    @Value("${discordbot.token}")
    private String botToken;
    @Autowired
    private WebClient.Builder wcb;
    @Autowired
    private RepoSteamMap repo;

    private void checkIfBotIsOnline() throws MarcoException {
        if (!botOnline) {
            throw new MarcoException(msgSource.getMessage("DISCBOT00001", null, LocaleContextHolder.getLocale()));
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
                        du.setId(m.getIdLong());
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
            .forEach(m -> guild.moveVoiceMember(m, v));
        // @formatter:on
        return true;
    }

    @Override
    public boolean balanceTheTeamsAndMoveThemInTheAppropirateChannel() throws MarcoException {
        checkIfBotIsOnline();

        Guild guild = jda.getGuildById(dsProps.getServerId());

        // @formatter:off
        List<DiscordUser> users = guild.loadMembers().get().parallelStream()
                .filter(m -> !m.getUser().isBot())
                .filter(m -> m.getVoiceState().inVoiceChannel()).map(m -> {
                    DiscordUser du = new DiscordUser();
                    du.setId(m.getIdLong());
                    du.setName(m.getUser().getName());
                    return du;
                }).collect(Collectors.toList());
        // @formatter:on

        /**
         * TODO
         * 
         * - Call the Round Service to split the team and move them into the appropriate
         * channel
         */

        return false;
    }

    @Override
    public List<Player> getListOfPlayers() throws MarcoException {
        // @formatter:off
        
        List<DiscordUser> discordUsers = getMembers();
        discordUsers.stream().forEach(d -> {
            if(repo.findById(d.getId()) == null) {
                EntitySteamMap entity = new EntitySteamMap();
                entity.setDiscordId(d.getId());
                entity.setDiscordName(d.getName());
                entity.setSteamId("");
                entity.setSteamName("");
                repo.persist(entity);
            }
        });
        
        return repo.getAll().stream()
            .map(e -> new Player(
                    new DiscordUser(e.getDiscordId(), e.getDiscordName()),
                    new SteamUser(e.getSteamId(), e.getSteamName())))
            .collect(Collectors.toList());
        // @formatter:on
    }

    @Override
    public boolean storePlayerDetails(Player player) throws MarcoException {
        EntitySteamMap entity = new EntitySteamMap();
        entity.setDiscordId(player.getDiscordDetails().getId());
        entity.setDiscordName(player.getDiscordDetails().getName());
        entity.setSteamId(player.getSteamDetails().getSteamId());
        entity.setSteamName(player.getSteamDetails().getUserName());
        return repo.persist(entity);
    }

    private ClientResponse performRequest(HttpMethod method, URL url, Map<String, String> headers,
            Map<String, String> queryParameters, MediaType contentType, Object body) {

        /*
         * Create the request and adds query parameters if provided
         */
        RequestBodySpec rbs = wcb.build().method(method).uri(uriBuilder -> {
            UriBuilder ub = uriBuilder.scheme(url.getProtocol()).host(url.getHost()).port(url.getPort())
                    .path(url.getPath());
            if (queryParameters != null) {
                for (Map.Entry<String, String> entry : queryParameters.entrySet()) {
                    ub = ub.queryParam(entry.getKey(), entry.getValue());
                }
            }
            return ub.build();

        }).contentType(contentType);

        /*
         * Add HTTP headers if provided
         */
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                rbs = rbs.header(entry.getKey(), entry.getValue());
            }
        }

        /*
         * Perform the call
         */
        ClientResponse resp = null;
        if (body != null) {
            resp = rbs.bodyValue(body).exchange().block();
        } else {
            resp = rbs.exchange().block();
        }
        return resp;
    }

    private <T> T getBodyFromResponse(ClientResponse response, Class<T> clazz) {
        return response.bodyToMono(clazz).block();
    }
}
