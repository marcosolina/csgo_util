package com.marco.discordbot.services.implementations;

import java.util.List;
import java.util.stream.Collectors;

import javax.security.auth.login.LoginException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import com.marco.discordbot.config.properties.DiscordServerProps;
import com.marco.discordbot.listeners.IxiGoDiscordListenerMarco;
import com.marco.discordbot.model.rest.DiscordUser;
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
        
        List<DiscordUser> users = guild.loadMembers().get().parallelStream()
        .filter(m -> !m.getUser().isBot())
        .filter(m -> m.getVoiceState().inVoiceChannel())
        .map(m -> {
            DiscordUser du = new DiscordUser();
            du.setId(m.getIdLong());
            du.setName(m.getUser().getName());
            return du;
        }).collect(Collectors.toList());
        
        
        /**
         * TODO 
         * 
         * - Call the Round Service to split the team and move them into the appropriate channel 
         */
        
        return false;
    }

}
