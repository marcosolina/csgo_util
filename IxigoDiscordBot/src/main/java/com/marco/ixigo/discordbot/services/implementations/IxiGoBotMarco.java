package com.marco.ixigo.discordbot.services.implementations;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.security.auth.login.LoginException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import com.marco.ixigo.discordbot.config.properties.DiscordProps;
import com.marco.ixigo.discordbot.enums.BotConfigKey;
import com.marco.ixigo.discordbot.listeners.IxiGoDiscordListenerMarco;
import com.marco.ixigo.discordbot.model.Player;
import com.marco.ixigo.discordbot.model.discord.BotConfig;
import com.marco.ixigo.discordbot.model.discord.DiscordUser;
import com.marco.ixigo.discordbot.repositories.interfaces.RepoUsersMap;
import com.marco.ixigo.discordbot.services.interfaces.IxiGoBot;
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
    private RepoUsersMap repo;
    @Autowired
    private DiscordProps dsProps;

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
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean moveAllMembersIntoGeneralChannel() throws MarcoException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean moveDiscordUsersInTheAppropirateChannel() throws MarcoException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean balanceTheTeams() throws MarcoException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean restartIxiGoRound() throws MarcoException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean warmUpBalanceTeamApi() throws MarcoException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setAutoBalance(boolean active) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public boolean isAutobalance() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public List<Player> getListOfMappedPlayers() throws MarcoException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean storePlayersDetails(List<Player> players) throws MarcoException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean updateBotConfig(BotConfig config) throws MarcoException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public BotConfig getBotConfig(BotConfigKey key) throws MarcoException {
        // TODO Auto-generated method stub
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
                .map(m -> repo.findById(m.getIdLong())) // Retrieve the Steam ID of the Discord member
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
