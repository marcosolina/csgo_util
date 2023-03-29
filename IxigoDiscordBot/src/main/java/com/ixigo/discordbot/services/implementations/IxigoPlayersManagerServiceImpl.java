package com.ixigo.discordbot.services.implementations;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import com.ixigo.discordbot.config.properties.PlayersManagerEndPoints;
import com.ixigo.discordbot.constants.ErrorCodes;
import com.ixigo.discordbot.services.interfaces.IxigoPlayersManagerService;
import com.ixigo.library.errors.IxigoException;
import com.ixigo.library.rest.interfaces.IxigoWebClientUtils;
import com.ixigo.playersmanagercontract.enums.GetTeamsQueryParam;
import com.ixigo.playersmanagercontract.enums.ScoreType;
import com.ixigo.playersmanagercontract.models.rest.RestTeams;

import reactor.core.publisher.Mono;

public class IxigoPlayersManagerServiceImpl implements IxigoPlayersManagerService {
	private static final Logger _LOGGER = LoggerFactory.getLogger(IxigoPlayersManagerServiceImpl.class);
	@Autowired
	private PlayersManagerEndPoints endPoints;
	@Autowired
	private IxigoWebClientUtils webClient;

	@Override
	public Mono<RestTeams> generateBalancedTeams(List<String> steamIds, Optional<Integer> numberOfMatches, Optional<Double> penaltyWeight, Optional<ScoreType> partitionScore, Optional<Double> minPercPlayed) throws IxigoException {
		_LOGGER.trace("Inside IxigoPlayersManagerServiceImpl.generateBalancedTeams");
		try {
			URL url = new URL(endPoints.getGetTeams());
			Map<String, String> queryParams = new HashMap<>();

			queryParams.put(GetTeamsQueryParam.USERS_STEAM_IDS.getQueryParamKey(), String.join(",", steamIds));

			if (numberOfMatches.isPresent()) {
				queryParams.put(GetTeamsQueryParam.NUMBER_OF_MATCHES.getQueryParamKey(), numberOfMatches.get().toString());
			}
			if (penaltyWeight.isPresent()) {
				queryParams.put(GetTeamsQueryParam.PENALTY_WEIGHT.getQueryParamKey(), penaltyWeight.get().toString());
			}
			if (partitionScore.isPresent()) {
				queryParams.put(GetTeamsQueryParam.PARTITION_SCORE_TYPE.getQueryParamKey(), partitionScore.get().getDesc());
			}
			if (minPercPlayed.isPresent()) {
				queryParams.put(GetTeamsQueryParam.MINIMUM_PERC_MATCH_PLAYED.getQueryParamKey(), minPercPlayed.get().toString());
			}

			return webClient.performGetRequest(RestTeams.class, url, Optional.empty(), Optional.of(queryParams)).map(resp -> resp.getBody());
		} catch (MalformedURLException e) {
			_LOGGER.error(e.getMessage());
			e.printStackTrace();
			throw new IxigoException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), ErrorCodes.GENERIC_ERROR);
		}
	}
}
