package com.ixigo.playersmanager.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.ixigo.library.mediators.web.interfaces.WebCommandHandler;
import com.ixigo.playersmanager.RestMapper;
import com.ixigo.playersmanager.commands.GetTeamsCmd;
import com.ixigo.playersmanager.services.interfaces.PlayersManager;
import com.ixigo.playersmanagercontract.models.rest.RestTeams;

import reactor.core.publisher.Mono;

@Component
public class GetTeamsCmdHandler implements WebCommandHandler<GetTeamsCmd, RestTeams> {
	private static final Logger _LOGGER = LoggerFactory.getLogger(GetTeamsCmdHandler.class);

	@Autowired
	private PlayersManager service;
	@Autowired
	private RestMapper mapper;

	@Override
	public Mono<ResponseEntity<RestTeams>> handle(GetTeamsCmd cmd) {
		_LOGGER.trace("Inside GetTeamsCmdHandler.handle");

		// @formatter:off
		return service.generateTwoTeamsForcingSimilarTeamSizes(
				cmd.getNumberOfMatches(),
				cmd.getSteamIDs(),
				cmd.getPenaltyWeigth(),
				cmd.getPartitionScore(),
				cmd.getMinPercPlayed())
			.collectList()
			.map(mapper::fromSvcTeamToRestList)
			.map(list -> {
				RestTeams teams = new RestTeams();
				teams.setTeams(list);
				return new ResponseEntity<>(teams, HttpStatus.OK);
			});
		// @formatter:on
	}

}
