package com.ixigo.playersmanager.controller;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ixigo.library.mediators.web.interfaces.WebMediator;
import com.ixigo.playersmanager.commands.GetTeamsCmd;
import com.ixigo.playersmanagercontract.enums.ScoreType;
import com.ixigo.playersmanagercontract.models.rest.RestTeams;

import io.swagger.annotations.ApiOperation;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/teams")
public class PlayersManagerController {
	private static final Logger _LOGGER = LoggerFactory.getLogger(PlayersManagerController.class);

	@Autowired
	private WebMediator mediator;

	@GetMapping
	@ApiOperation(value = "It will split the \"steamIDs\" into two teams using the avarage score of the last \"numberOfMatches\" played by the users")
	// @formatter:off
    public Mono<ResponseEntity<RestTeams>> getTeams(
            @RequestParam(name = "numberOfMatches", defaultValue = "50") Integer numberOfMatches,
            @RequestParam("steamIDs") List<String> steamIDs,
            @RequestParam(name = "penaltyWeigth", defaultValue = "0.4") Double penaltyWeigth,
            @RequestParam(name = "partitionScore", defaultValue = "HLTV") ScoreType partitionScore,
            @RequestParam(name = "minPercPlayed", defaultValue = "0.9") BigDecimal minPercPlayed) {
		_LOGGER.trace("Inside PlayersManagerController.getTeams");
		return mediator.send(GetTeamsCmd.builder()
			.numberOfMatches(numberOfMatches)
			.steamIDs(steamIDs)
			.penaltyWeigth(penaltyWeigth)
			.partitionScore(partitionScore)
			.minPercPlayed(minPercPlayed)
			.build()
		);
		// @formatter:on
	}
}
