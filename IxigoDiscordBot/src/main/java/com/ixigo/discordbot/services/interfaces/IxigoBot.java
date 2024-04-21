package com.ixigo.discordbot.services.interfaces;

import java.util.List;

import com.ixigo.discordbot.models.svc.discord.SvcBotConfig;
import com.ixigo.discordbot.models.svc.discord.SvcDiscordUser;
import com.ixigo.discordbot.models.svc.discord.SvcPlayer;
import com.ixigo.enums.BotConfigKey;
import com.ixigo.library.errors.IxigoException;

import net.dv8tion.jda.api.entities.MessageEmbed;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IxigoBot {
	/**
	 * But the bot online
	 * 
	 * @return
	 * @throws MarcoException
	 */
	public abstract Mono<Boolean> start() throws IxigoException;

	/**
	 * Disconnect the bot
	 * 
	 * @return
	 * @throws MarcoException
	 */
	public abstract Mono<Boolean> stop() throws IxigoException;

	public abstract Flux<SvcDiscordUser> getDiscordUsers() throws IxigoException;

	public abstract void moveAllMembersIntoGeneralChannel() throws IxigoException;

	public abstract void moveDiscordUsersInTheAppropriateChannel() throws IxigoException;
	
	public abstract void moveDiscordUsersInTheAppropriateChannelCs2() throws IxigoException;

	public abstract Mono<Boolean> balanceTheTeams() throws IxigoException;
	
	public abstract Mono<Boolean> balanceTheTeamsCs2() throws IxigoException;

	public abstract Mono<Boolean> kickTheBots() throws IxigoException;
	
	public abstract Mono<Boolean> kickTheBotsCs2() throws IxigoException;

	public abstract Mono<Boolean> restartCsgoMatch() throws IxigoException;
	
	public abstract Mono<Boolean> restartCs2Match() throws IxigoException;

	public abstract Mono<Boolean> warmUpBalanceTeamApi() throws IxigoException;

	public abstract Flux<SvcPlayer> getListOfMappedPlayers() throws IxigoException;

	public abstract Mono<Boolean> storePlayersDetails(List<SvcPlayer> players) throws IxigoException;

	public abstract Mono<Boolean> updateBotConfig(SvcBotConfig config) throws IxigoException;

	public abstract Mono<SvcBotConfig> getBotConfig(BotConfigKey key) throws IxigoException;
	public abstract Flux<SvcBotConfig> getBotConfigAll() throws IxigoException;
	
	public abstract Mono<Void> makeTeamsAndMoveToVoiceChannel() throws IxigoException;
	
	public abstract Mono<Void> balanceMembersInVoiceChannel() throws IxigoException;
	
	public abstract void sendMessageToGeneralChat(String msg);
	public abstract void sendEmbedMessageToGeneralChat(MessageEmbed me);
}
