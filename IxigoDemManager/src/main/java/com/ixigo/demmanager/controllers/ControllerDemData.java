package com.ixigo.demmanager.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ixigo.demmanager.commands.demdata.CmdGetUsers;
import com.ixigo.demmanager.commands.demdata.CmdGetUsersLastScores;
import com.ixigo.demmanagercontract.models.rest.demdata.responses.RestUsersResp;
import com.ixigo.demmanagercontract.models.rest.demdata.responses.RestUsersScoresResp;
import com.ixigo.library.mediators.web.interfaces.WebMediator;

import io.swagger.annotations.ApiOperation;
import reactor.core.publisher.Mono;

/**
 * Controller used to expose the APIs to access the data extracted from the DEM
 * files
 * 
 * @author marco
 *
 */
@RestController
@RequestMapping("/demdata")
public class ControllerDemData {
	private static final Logger _LOGGER = LoggerFactory.getLogger(ControllerDemData.class);

	@Autowired
	private WebMediator mediator;

	@GetMapping("/users")
	@ApiOperation(value = "It returns a list of the known users")
	public Mono<ResponseEntity<RestUsersResp>> getUsers() {
		_LOGGER.trace("Inside ControllerDemData.getUsers");
		return mediator.send(new CmdGetUsers());
	}

	/*
	/**
	 * It will return the last "counter" number of scores associated to the Users
	 * provided in the input list
	 * 
	 * @param counter
	 * @param usersIDs
	 * @return
	 *
	 */
	@GetMapping("/usersscores")
	@ApiOperation(value = "It will return the users scores for the last \"numberOfMatches\" where the players have played for at least \"minPercPlayed\" of the the match")
	// @formatter:off
    public Mono<ResponseEntity<RestUsersScoresResp>> getUsersLastScores(
            @RequestParam(name = "numberOfMatches", defaultValue = "50") Integer numberOfMatches,
            @RequestParam("usersIDs") List<String> usersIDs
            ) {
	// @formatter:on
		_LOGGER.trace("Inside ControllerDemData.getUsersLastScores");
		return mediator.send(new CmdGetUsersLastScores(numberOfMatches, usersIDs));
	}
}
