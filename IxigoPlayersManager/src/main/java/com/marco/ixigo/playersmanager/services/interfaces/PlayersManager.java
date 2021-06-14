package com.marco.ixigo.playersmanager.services.interfaces;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.marco.ixigo.playersmanager.enums.ScoreType;
import com.marco.ixigo.playersmanager.models.dto.Team;
import com.marco.ixigo.playersmanager.models.dto.UserAvgScore;
import com.marco.utils.MarcoException;

public interface PlayersManager {

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
    public List<Team> generateTeams(Integer teamsCounter, Integer gamesCounter, List<String> usersIDs,
            ScoreType scoreType, BigDecimal minPercPlayed) throws MarcoException;

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
            List<String> usersIDs, double penaltyWeigth, ScoreType scoreType, BigDecimal minPercPlayed)
            throws MarcoException;

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
            double penaltyWeigth, ScoreType scoreType, BigDecimal minPercPlayed) throws MarcoException;
    
    /**
     * It will return the Users Average scored calculated using the most recent
     * "gamesCounter" games
     * 
     * @param gamesCounter
     * @param usersIDs
     * @return
     * @throws MarcoException
     */
    public Map<String, UserAvgScore> getUsersAvgStatsForLastXGames(Integer gamesCounter, List<String> usersIDs,
            ScoreType partionByScore, BigDecimal minPercPlayed) throws MarcoException;
}
