package com.marco.csgoutil.roundparser.controllers;

import java.math.BigDecimal;
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

import com.marco.csgoutil.roundparser.enums.PartitionType;
import com.marco.csgoutil.roundparser.enums.ScoreType;
import com.marco.csgoutil.roundparser.model.rest.AvailableGames;
import com.marco.csgoutil.roundparser.model.rest.MapsScores;
import com.marco.csgoutil.roundparser.model.rest.ScoreTypes;
import com.marco.csgoutil.roundparser.model.rest.Teams;
import com.marco.csgoutil.roundparser.model.rest.UserScores;
import com.marco.csgoutil.roundparser.model.rest.UserSvgScores;
import com.marco.csgoutil.roundparser.model.rest.Users;
import com.marco.csgoutil.roundparser.model.rest.UsersScores;
import com.marco.csgoutil.roundparser.model.service.Team;
import com.marco.csgoutil.roundparser.services.interfaces.RoundsService;
import com.marco.csgoutil.roundparser.utils.RoundParserUtils;
import com.marco.utils.MarcoException;

import io.swagger.annotations.ApiOperation;

/**
 * Standard SpringBoot controller
 * 
 * @author Marco
 *
 */
@Controller
public class MainController {

	private static final Logger _LOGGER = LoggerFactory.getLogger(MainController.class);

	@Autowired
	private RoundsService service;

	/**
	 * It will trigger the scan process for new .dem files
	 * 
	 * @return
	 */
	@PostMapping(RoundParserUtils.MAPPING_ADD_NEW_DATA)
	@ApiOperation(value = "It will trigger the scan process for new .dem files")
	public ResponseEntity<MapsScores> addNewScores() {
		_LOGGER.trace("Inside MainController.addNewScores");

		MapsScores resp = new MapsScores();
		try {

			resp.setMapScores(service.processNewDemFiles());
			resp.setStatus(true);
			return new ResponseEntity<>(resp, HttpStatus.OK);
		} catch (MarcoException e) {
			resp.addError(e);
			return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping(RoundParserUtils.MAPPING_GET_SCORES_TYPE)
	@ApiOperation(value = "It will return a map of the score types that you can pass when call the team creation API")
	public ResponseEntity<ScoreTypes> getScoreType() {
		_LOGGER.trace("Inside MainController.getScoreType");

		ScoreTypes resp = new ScoreTypes();
		resp.setTypes(service.mapOfAvailableScores());
		resp.setStatus(true);
		return new ResponseEntity<>(resp, HttpStatus.OK);
	}

	/**
	 * It returns a list of the known users
	 * 
	 * @return
	 */
	@GetMapping(RoundParserUtils.MAPPING_GET_USERS)
	@ApiOperation(value = "It returns a list of the known users")
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

	/**
	 * It will return all the user scores
	 * 
	 * @param steamId
	 * @return
	 */
	@GetMapping(RoundParserUtils.MAPPING_GET_USER_SCORES)
	@ApiOperation(value = "It will return all the user scores")
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

	/**
	 * It will return the last "counter" number of scores associated to the Users
	 * provided in the input list
	 * 
	 * @param counter
	 * @param usersIDs
	 * @return
	 */
	@GetMapping(RoundParserUtils.MAPPING_GET_USER_LAST_SCORES)
	@ApiOperation(value = "It will return the last \"counter\" number of scores associated to the Users provided in the input list")
	public ResponseEntity<UsersScores> getUsersLastScores(@PathVariable("counter") Integer counter,
			@RequestParam("usersIDs") List<String> usersIDs,
			@RequestParam(name = "minPercPlayed", defaultValue = "0") BigDecimal minPercPlayed) {
		_LOGGER.trace("Inside MainController.getUsersLastScores");

		UsersScores resp = new UsersScores();
		try {
			resp.setUsersScores(service.getUsersStatsForLastXGames(counter, usersIDs, minPercPlayed));
			resp.setStatus(true);
			return new ResponseEntity<>(resp, HttpStatus.OK);
		} catch (MarcoException e) {
			resp.addError(e);
			return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * It will return the "average" score of the users provided in the input list.
	 * The average will be calculated on the last "counter" games
	 * 
	 * @param counter
	 * @param usersIDs
	 * @return
	 */
	@GetMapping(RoundParserUtils.MAPPING_GET_USER_AVG_SCORES)
	@ApiOperation(value = "It will return the \"average\" score of the users provided in the input list. "
			+ "The average will be calculated on the last \"counter\" games")
	public ResponseEntity<UserSvgScores> getUsersAvgScores(@PathVariable("counter") Integer counter,
			@RequestParam("usersIDs") List<String> usersIDs,
			@RequestParam(name = "minPercPlayed", defaultValue = "0") BigDecimal minPercPlayed) {
		_LOGGER.trace("Inside MainController.getUsersAvgScores");

		UserSvgScores resp = new UserSvgScores();
		try {
			resp.setAvgScores(service.getUsersAvgStatsForLastXGames(counter, usersIDs, ScoreType.RWS, minPercPlayed));
			resp.setStatus(true);
			return new ResponseEntity<>(resp, HttpStatus.OK);
		} catch (MarcoException e) {
			resp.addError(e);
			return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * It will calculate the "average" score of the users provided in the input
	 * list. The average will be calculated on the last "counter" games and then it
	 * will will "equally" split the users in different in different teams
	 * (teamsCounter)
	 * 
	 * @param teamsCounter
	 * @param counter
	 * @param usersIDs
	 * @return
	 */
	@GetMapping(RoundParserUtils.MAPPING_GET_TEAMS)
	@ApiOperation(value = "It will calculate the \"average\" score of the users provided in the input list. "
			+ "The average will be calculated on the last \"counter\" games and then it will will \"equally\" "
			+ "split the users in different in different teams (teamsCounter) ")
	public ResponseEntity<Teams> getTeams(@PathVariable("teamsCounter") Integer teamsCounter,
			@PathVariable("counter") Integer counter, @RequestParam("usersIDs") List<String> usersIDs,
			@RequestParam(name = "partitionType", defaultValue = "SIMPLE") PartitionType partitionType,
			@RequestParam(name = "penaltyWeigth", defaultValue = "0") Double penaltyWeigth,
			@RequestParam(name = "partitionScore", defaultValue = "RWS") ScoreType partitionScore,
			@RequestParam(name = "minPercPlayed", defaultValue = "0") BigDecimal minPercPlayed) {
		_LOGGER.trace("Inside MainController.getTeams");

		Teams resp = new Teams();
		try {
			List<Team> teams = null;
			switch (partitionType) {
			case IXIGO:
				teams = service.generateTwoTeamsForcingSimilarTeamSizes(counter, usersIDs, penaltyWeigth, partitionScore, minPercPlayed);
				break;
			case FORCE_PLAYERS:
				teams = service.generateTeamsForcingSimilarTeamSizes(teamsCounter, counter, usersIDs, penaltyWeigth,
						partitionScore, minPercPlayed);
				break;
			case SIMPLE:
				teams = service.generateTeams(teamsCounter, counter, usersIDs, partitionScore, minPercPlayed);
				break;
			default:
				break;
			}
			resp.setTeams(teams);
			resp.setStatus(true);
			return new ResponseEntity<>(resp, HttpStatus.OK);
		} catch (MarcoException e) {
			resp.addError(e);
			return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping(RoundParserUtils.MAPPING_GET_GAMES_LIST)
	@ApiOperation(value = "It returns the full list of available games")
	public ResponseEntity<AvailableGames> getListAvailableGAmesTeams() {
		_LOGGER.trace("Inside MainController.getListAvailableGAmesTeams");

		AvailableGames resp = new AvailableGames();
		resp.setAvailableGames(service.getAvailableGamesList());
		resp.setStatus(true);
		return new ResponseEntity<>(resp, HttpStatus.OK);
	}

}
