package com.ixigo.discordbot.services.interfaces;

import java.util.List;
import java.util.Optional;

import com.ixigo.library.errors.IxigoException;
import com.ixigo.playersmanagercontract.enums.ScoreType;
import com.ixigo.playersmanagercontract.models.rest.RestTeams;

import reactor.core.publisher.Mono;

public interface IxigoPlayersManagerService {
	// @formatter:off
	public Mono<RestTeams> generateBalancedTeams(
			List<String> steamIds,
			Optional<Integer> numberOfMatches,
			Optional<Double> penaltyWeight,
			Optional<ScoreType> partitionScore,
			Optional<Double> minPercPlayed
			) throws IxigoException;
	// @formatter:on
}
