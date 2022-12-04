package com.ixigo.discordbot.services.implementations;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ixigo.discordbot.enums.BotConfigKey;
import com.ixigo.discordbot.models.svc.discord.DiscordUser;
import com.ixigo.discordbot.models.svc.discord.SvcBotConfig;
import com.ixigo.discordbot.models.svc.discord.SvcPlayer;
import com.ixigo.discordbot.services.interfaces.IxigoBot;
import com.ixigo.library.errors.IxigoException;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class IxigoBotImpl implements IxigoBot {
	private static final Logger _LOGGER = LoggerFactory.getLogger(IxigoBotImpl.class);

	@Override
	public Mono<Boolean> start() throws IxigoException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Mono<Boolean> stop() throws IxigoException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Flux<DiscordUser> getDiscordOnlineUsers() throws IxigoException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Mono<Boolean> moveAllMembersIntoGeneralChannel() throws IxigoException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Mono<Boolean> moveDiscordUsersInTheAppropriateChannel() throws IxigoException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Mono<Boolean> balanceTheTeams() throws IxigoException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Mono<Boolean> kickTheBots() throws IxigoException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Mono<Boolean> restartCsgpMatch() throws IxigoException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Mono<Boolean> warmUpBalanceTeamApi() throws IxigoException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Mono<Void> setAutoBalance(boolean active) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Mono<Boolean> isAutobalance() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Mono<Boolean> isKickBots() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Mono<Void> setKickBots(boolean active) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Flux<SvcPlayer> getListOfMappedPlayers() throws IxigoException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Mono<Boolean> storePlayersDetails(List<SvcPlayer> players) throws IxigoException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Mono<Boolean> updateBotConfig(SvcBotConfig config) throws IxigoException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Mono<SvcBotConfig> getBotConfig(BotConfigKey key) throws IxigoException {
		// TODO Auto-generated method stub
		return null;
	}

}
