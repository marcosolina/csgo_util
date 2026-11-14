package com.ixigo.demmanager.handlers.demdata;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.ixigo.demmanager.commands.demdata.CmdGetUsersLastScores;
import com.ixigo.demmanager.mappers.RestMapper;
import com.ixigo.demmanager.services.interfaces.DemFileParser;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestPlayerMatchStatsExtended;
import com.ixigo.demmanagercontract.models.rest.demdata.responses.RestUsersScoresResp;
import com.ixigo.library.mediators.web.interfaces.WebCommandHandler;

import reactor.core.publisher.Mono;

/**
 * Handler used to retrieve the scores of the players according to the input
 * filters
 * 
 * @author marco
 *
 */
@Component
public class GetUsersLastScoresHandler implements WebCommandHandler<CmdGetUsersLastScores, RestUsersScoresResp> {
	private static final Logger _LOGGER = LoggerFactory.getLogger(GetUsersLastScoresHandler.class);
	@Autowired
	private DemFileParser service;
	@Autowired
	private RestMapper mapper;

	@Override
	public Mono<ResponseEntity<RestUsersScoresResp>> handle(CmdGetUsersLastScores request) {
		_LOGGER.trace("Inside GetUsersLastScoresHandler.handle");

		// @formatter:off
		return service.getUsersStatsForLastXGames(request.getNumberOfMatches(), request.getUsersIDs())
				.collectList().map(list -> {
					Map<String, List<RestPlayerMatchStatsExtended>> map = new HashMap<>();
					// Save the statistics into a map where the key is the user steam ID and the value the list of statistics
					list.forEach(stat -> {
						var restList = stat.getScores().stream().map(mapper::fromSvcToRest).collect(Collectors.toList());
						map.put(stat.getSteamId(), restList);
					});
		
					RestUsersScoresResp resp = new RestUsersScoresResp();
					resp.setUserScores(map);
					return new ResponseEntity<>(resp, HttpStatus.OK);
				});
		// @formatter:on
	}

}
