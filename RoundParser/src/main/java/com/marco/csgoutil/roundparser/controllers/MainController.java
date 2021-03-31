package com.marco.csgoutil.roundparser.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.marco.csgoutil.roundparser.model.rest.MapsScores;
import com.marco.csgoutil.roundparser.model.rest.Teams;
import com.marco.csgoutil.roundparser.model.rest.UserScores;
import com.marco.csgoutil.roundparser.model.rest.UserSvgScores;
import com.marco.csgoutil.roundparser.model.rest.Users;
import com.marco.csgoutil.roundparser.model.rest.UsersScores;
import com.marco.csgoutil.roundparser.services.interfaces.RoundsService;
import com.marco.csgoutil.roundparser.utils.RoundParserUtils;
import com.marco.utils.MarcoException;

@Controller
public class MainController {

	private static final Logger _LOGGER = LoggerFactory.getLogger(MainController.class);
	
	@Autowired
	private RoundsService service;
	
	@PostMapping(RoundParserUtils.MAPPING_ADD_NEW_DATA)
	public ResponseEntity<MapsScores> addNewScores() {
		_LOGGER.trace("Inside MainController.addNewScores");
		
		MapsScores resp = new MapsScores();
		try {
			
			resp.setMapScores(service.processAllDemFiles());
			resp.setStatus(true);
			return new ResponseEntity<>(resp, HttpStatus.OK);
		} catch (MarcoException e) {
			resp.addError(e);
			return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping(RoundParserUtils.MAPPING_GET_USERS)
	public ResponseEntity<Users> getUsers() {
		_LOGGER.trace("Inside MainController.getUsers");
		
		Users resp = new Users();
		try {
			
			resp.setUsers(service.getListOfUsers());
			resp.setStatus(true);
			return new ResponseEntity<>(resp, HttpStatus.OK);
		} catch (MarcoException e) {
			resp.addError(e);
			return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping(RoundParserUtils.MAPPING_GET_USER_SCORES)
	public ResponseEntity<UserScores> getUserScores(@PathVariable("steamID") String steamId) {
		_LOGGER.trace("Inside MainController.getUserScores");
		
		UserScores resp = new UserScores();
		try {
			
			resp.setMapsScores(service.getUserStats(steamId));
			resp.setStatus(true);
			return new ResponseEntity<>(resp, HttpStatus.OK);
		} catch (MarcoException e) {
			resp.addError(e);
			return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping(RoundParserUtils.MAPPING_GET_USER_LAST_SCORES)
	public ResponseEntity<UsersScores> getUsersLastScores(@PathVariable("counter") Integer counter, @RequestParam("usersIDs") List<String> usersIDs) {
		_LOGGER.trace("Inside MainController.getUsersLastScores");
		
		UsersScores resp = new UsersScores();
		try {
			resp.setUsersScores(service.getUsersStatsForLastXGames(counter, usersIDs));
			resp.setStatus(true);
			return new ResponseEntity<>(resp, HttpStatus.OK);
		} catch (MarcoException e) {
			resp.addError(e);
			return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping(RoundParserUtils.MAPPING_GET_USER_AVG_SCORES)
	public ResponseEntity<UserSvgScores> getUsersAvgScores(@PathVariable("counter") Integer counter, @RequestParam("usersIDs") List<String> usersIDs) {
		_LOGGER.trace("Inside MainController.getUsersAvgScores");
		
		UserSvgScores resp = new UserSvgScores();
		try {
			resp.setAvgScores(service.getUsersAvgStatsForLastXGames(counter, usersIDs));
			resp.setStatus(true);
			return new ResponseEntity<>(resp, HttpStatus.OK);
		} catch (MarcoException e) {
			resp.addError(e);
			return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping(RoundParserUtils.MAPPING_GET_TEAMS)
	public ResponseEntity<Teams> getTeams(@PathVariable("teamsCounter") Integer teamsCounter, @PathVariable("counter") Integer counter, @RequestParam("usersIDs") List<String> usersIDs) {
		_LOGGER.trace("Inside MainController.getTeams");
		
		Teams resp = new Teams();
		try {
			resp.setTeams(service.generateTeams(teamsCounter, counter, usersIDs));
			resp.setStatus(true);
			return new ResponseEntity<>(resp, HttpStatus.OK);
		} catch (MarcoException e) {
			resp.addError(e);
			return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
		}
	}
	
}
