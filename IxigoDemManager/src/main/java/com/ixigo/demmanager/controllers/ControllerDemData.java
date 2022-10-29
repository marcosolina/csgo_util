package com.ixigo.demmanager.controllers;

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

import com.ixigo.demmanager.commands.demdata.CmdGetMapPlayedCount;
import com.ixigo.demmanager.commands.demdata.CmdGetScoreType;
import com.ixigo.demmanager.commands.demdata.CmdGetUsers;
import com.ixigo.demmanager.commands.demdata.CmdGetUsersLastScores;
import com.ixigo.demmanagercontract.models.rest.demdata.RestMapsPlayed;
import com.ixigo.demmanagercontract.models.rest.demdata.RestScoreTypes;
import com.ixigo.demmanagercontract.models.rest.demdata.RestUsers;
import com.ixigo.demmanagercontract.models.rest.demdata.RestUsersScores;
import com.ixigo.library.mediators.web.interfaces.WebMediator;

import io.swagger.annotations.ApiOperation;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/demdata")
public class ControllerDemData {
	private static final Logger _LOGGER = LoggerFactory.getLogger(ControllerDemData.class);

	@Autowired
	private WebMediator mediator;

	@GetMapping("/scorestype")
	@ApiOperation(value = "It will return a map of the score types that you can pass when call the team creation API")
	public Mono<ResponseEntity<RestScoreTypes>> getScoreType() {
		_LOGGER.trace("Inside ControllerDemData.getScoreType");
		return mediator.send(new CmdGetScoreType());
	}

	@GetMapping("/mapsplayed")
	@ApiOperation(value = "It will returns how many times we played a map")
	public Mono<ResponseEntity<RestMapsPlayed>> getMapPlayedCount() {
		_LOGGER.trace("Inside ControllerDemData.getMapPlayedCount");
		return mediator.send(new CmdGetMapPlayedCount());
	}

	@GetMapping("/users")
	@ApiOperation(value = "It returns a list of the known users")
	public Mono<ResponseEntity<RestUsers>> getUsers() {
		_LOGGER.trace("Inside ControllerDemData.getUsers");
		return mediator.send(new CmdGetUsers());
	}

	/**
	 * It will return the last "counter" number of scores associated to the Users
	 * provided in the input list
	 * 
	 * @param counter
	 * @param usersIDs
	 * @return
	 */
	@GetMapping("/usersscores")
	@ApiOperation(value = "It will return the last \"counter\" number of scores associated to the Users provided in the input list")
	// @formatter:off
    public Mono<ResponseEntity<RestUsersScores>> getUsersLastScores(
            @RequestParam(name = "counter", defaultValue = "50") Integer counter,
            @RequestParam("usersIDs") List<String> usersIDs,
            @RequestParam(name = "minPercPlayed", defaultValue = "0") BigDecimal minPercPlayed) {
	// @formatter:on
		_LOGGER.trace("Inside ControllerDemData.getUsersLastScores");
		return mediator.send(new CmdGetUsersLastScores(counter, usersIDs, minPercPlayed));
	}
}
