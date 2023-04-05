package com.ixigo.demmanager.handlers.demdata;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.ixigo.demmanager.commands.demdata.CmdGetUsersLastScores;
import com.ixigo.demmanager.mappers.RestMapper;
import com.ixigo.demmanager.services.interfaces.DemFileParser;
import com.ixigo.demmanagercontract.models.rest.demdata.RestMapStats;
import com.ixigo.demmanagercontract.models.rest.demdata.RestUsersScores;
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
public class GetUsersLastScoresHandler implements WebCommandHandler<CmdGetUsersLastScores, RestUsersScores> {
	private static final Logger _LOGGER = LoggerFactory.getLogger(GetUsersLastScoresHandler.class);
	@Autowired
	private DemFileParser service;
	@Autowired
	private RestMapper mapper;

	@Override
	public Mono<ResponseEntity<RestUsersScores>> handle(CmdGetUsersLastScores request) {
		_LOGGER.trace("Inside GetUsersLastScoresHandler.handle");

		// @formatter:off
		return service.getUsersStatsForLastXGames(request.getNumberOfMatches(), request.getUsersIDs(), request.getMinPercPlayed())
				.collectList().map(list -> {
					Map<String, List<RestMapStats>> map = new HashMap<>();
					// Save the statistics into a map where the key is the user steam ID and the value the list of statistics
					list.forEach(stat -> {
						map.put(stat.getSteamId(), mapper.fromSvcToRestMapStats(stat.getStats()));
					});
		
					RestUsersScores resp = new RestUsersScores();
					resp.setUsersScores(map);
					return new ResponseEntity<>(resp, HttpStatus.OK);
				});
		// @formatter:on
	}

}
