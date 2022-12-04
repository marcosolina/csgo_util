package com.ixigo.discordbot.services.implementations;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ixigo.demmanagercontract.models.rest.demdata.RestUser;
import com.ixigo.discordbot.enums.TeamType;
import com.ixigo.discordbot.services.interfaces.IxigoRconService;
import com.ixigo.library.errors.IxigoException;
import com.ixigo.playersmanagercontract.models.rest.RestTeams;

import reactor.core.publisher.Mono;

public class IxigoRconServiceImpl implements IxigoRconService {
	private static final Logger _LOGGER = LoggerFactory.getLogger(IxigoRconServiceImpl.class);

	@Override
	public Mono<Map<TeamType, List<RestUser>>> getCurrentActivePlayersOnTheIxiGoServer() throws IxigoException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Mono<Boolean> kickTheBots() throws IxigoException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Mono<Boolean> restartIxiGoRound() throws IxigoException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Mono<Boolean> movePlayersToAppropriateTeam(RestTeams teams) throws IxigoException {
		// TODO Auto-generated method stub
		return null;
	}

}
