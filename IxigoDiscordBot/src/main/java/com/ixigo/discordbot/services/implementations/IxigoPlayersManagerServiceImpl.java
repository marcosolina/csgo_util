package com.ixigo.discordbot.services.implementations;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ixigo.discordbot.services.interfaces.IxigoPlayersManagerService;
import com.ixigo.library.errors.IxigoException;
import com.ixigo.playersmanagercontract.enums.ScoreType;
import com.ixigo.playersmanagercontract.models.rest.RestTeams;

import reactor.core.publisher.Mono;

public class IxigoPlayersManagerServiceImpl implements IxigoPlayersManagerService {
	private static final Logger _LOGGER = LoggerFactory.getLogger(IxigoPlayersManagerServiceImpl.class);

	@Override
	public Mono<RestTeams> generateBalancedTeams(List<String> steamIds, Optional<Integer> numberOfMatches, Optional<Double> penaltyWeight, Optional<ScoreType> partitionScore, Optional<Integer> minPercPlayed) throws IxigoException {
		// TODO Auto-generated method stub
		return null;
	}

}
