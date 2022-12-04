package com.ixigo.discordbot.services.interfaces;

import java.util.List;

import com.ixigo.discordbot.enums.BotConfigKey;
import com.ixigo.discordbot.models.svc.discord.DiscordUser;
import com.ixigo.discordbot.models.svc.discord.SvcBotConfig;
import com.ixigo.discordbot.models.svc.discord.SvcPlayer;
import com.ixigo.library.errors.IxigoException;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IxigoBot {
	/**
     * But the bot online
     * 
     * @return
     * @throws MarcoException
     */
    public Mono<Boolean> start() throws IxigoException;
    
    /**
     * Disconnect the bot
     * 
     * @return
     * @throws MarcoException
     */
    public Mono<Boolean> stop() throws IxigoException;
    
    public Flux<DiscordUser> getDiscordOnlineUsers() throws IxigoException;
    public Mono<Boolean> moveAllMembersIntoGeneralChannel() throws IxigoException;
    public Mono<Boolean> moveDiscordUsersInTheAppropriateChannel() throws IxigoException;
    public Mono<Boolean> balanceTheTeams() throws IxigoException;
    public Mono<Boolean> kickTheBots() throws IxigoException;
    public Mono<Boolean> restartCsgpMatch() throws IxigoException;
    public Mono<Boolean> warmUpBalanceTeamApi() throws IxigoException;
    public Mono<Void> setAutoBalance(boolean active);
    public Mono<Boolean> isAutobalance();
    public Mono<Boolean> isKickBots();
    public Mono<Void> setKickBots(boolean active);
    public Flux<SvcPlayer> getListOfMappedPlayers() throws IxigoException;
    public Mono<Boolean> storePlayersDetails(List<SvcPlayer> players) throws IxigoException;
    public Mono<Boolean> updateBotConfig(SvcBotConfig config) throws IxigoException;
    public Mono<SvcBotConfig> getBotConfig(BotConfigKey key) throws IxigoException;
}
