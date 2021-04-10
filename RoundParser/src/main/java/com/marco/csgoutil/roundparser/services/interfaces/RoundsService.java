package com.marco.csgoutil.roundparser.services.interfaces;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.marco.csgoutil.roundparser.enums.ScoreType;
import com.marco.csgoutil.roundparser.model.rest.User;
import com.marco.csgoutil.roundparser.model.rest.UserAvgScore;
import com.marco.csgoutil.roundparser.model.service.MapStats;
import com.marco.csgoutil.roundparser.model.service.Team;
import com.marco.utils.MarcoException;

/**
 * It provides the functionalities used to extract / calculate the games
 * information
 * 
 * @author Marco
 *
 */
public interface RoundsService {

	/**
	 * If new dem files are available, it will process them and return the relevant
	 * information
	 * 
	 * @return
	 * @throws MarcoException
	 */
	public List<MapStats> processNewDemFiles() throws MarcoException;

	/**
	 * It will parse the dem file and extract the informations
	 * 
	 * @param f
	 * @return
	 * @throws MarcoException
	 */
	public MapStats generateMapStatFromFile(File f) throws MarcoException;

	/**
	 * It will return a list of all the known users
	 * 
	 * @return
	 * @throws MarcoException
	 */
	public List<User> getListOfUsers() throws MarcoException;

	/**
	 * It will returns all the stats available for the selected user
	 * 
	 * @param steamId
	 * @return
	 * @throws MarcoException
	 */
	public List<MapStats> getUserStats(String steamId) throws MarcoException;

	/**
	 * It will return all the Users scores for the most recent "gamesCounter" games
	 * 
	 * @param gamesCounter
	 * @param usersIDs
	 * @return
	 * @throws MarcoException
	 */
	public Map<String, List<MapStats>> getUsersStatsForLastXGames(Integer gamesCounter, List<String> usersIDs)
			throws MarcoException;

	/**
	 * It will return the Users Average scored calculated using the most recent
	 * "gamesCounter" games
	 * 
	 * @param gamesCounter
	 * @param usersIDs
	 * @return
	 * @throws MarcoException
	 */
	public Map<String, UserAvgScore> getUsersAvgStatsForLastXGames(Integer gamesCounter, List<String> usersIDs, ScoreType partionByScore)
			throws MarcoException;

	/**
	 * It will generate "teamsCounter" number of teams. It will calculate the
	 * average score of the provided list of users calculated on the last
	 * "gamesCounter" games
	 * 
	 * @param teamsCounter
	 * @param gamesCounter
	 * @param usersIDs
	 * @return
	 * @throws MarcoException
	 */
	public List<Team> generateTeams(Integer teamsCounter, Integer gamesCounter, List<String> usersIDs, ScoreType scoreType)
			throws MarcoException;

	/**
	 * It works similar to "generateTeams", but if one team have more players (delta
	 * > 1 ) it will increase by one the avg score of the "weakest" player and try
	 * to create again the teams
	 * 
	 * @param teamsCounter
	 * @param gamesCounter
	 * @param usersIDs
	 * @return
	 * @throws MarcoException
	 */
	public List<Team> generateTeamsForcingSimilarTeamSizes(Integer teamsCounter, Integer gamesCounter,
			List<String> usersIDs, double penaltyWeigth, ScoreType scoreType) throws MarcoException;

	/**
	 * It will generate two teams and add some penalty to make even teams
	 * 
	 * @param gamesCounter
	 * @param usersIDs
	 * @param penaltyWeigth
	 * @return
	 * @throws MarcoException
	 */
	public List<Team> generateTwoTeamsForcingSimilarTeamSizes(Integer gamesCounter, List<String> usersIDs,
			double penaltyWeigth, ScoreType scoreType) throws MarcoException;

	/**
	 * It returns the list off available games stored into the system
	 * 
	 * @return
	 */
	public List<LocalDateTime> getAvailableGamesList();

}
