package com.ixigo.playersmanager.services.interfaces;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.ixigo.enums.ScoreType;
import com.ixigo.library.errors.IxigoException;
import com.ixigo.playersmanager.models.svc.SvcTeam;
import com.ixigo.playersmanager.models.svc.SvcUserAvgScore;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
    public Flux<SvcTeam> generateTeams(Integer teamsCounter, Integer gamesCounter, List<String> usersIDs,
            ScoreType scoreType, BigDecimal minPercPlayed) throws IxigoException;

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
    public Flux<SvcTeam> generateTeamsForcingSimilarTeamSizes(Integer teamsCounter, Integer gamesCounter,
            List<String> usersIDs, double penaltyWeigth, ScoreType scoreType, BigDecimal minPercPlayed)
            throws IxigoException;

    /**
     * It will generate two teams and add some penalty to make even teams
     * 
     * @param gamesCounter
     * @param usersIDs
     * @param penaltyWeigth
     * @return
     * @throws MarcoException
     */
    public Flux<SvcTeam> generateTwoTeamsForcingSimilarTeamSizes(Integer gamesCounter, List<String> usersIDs,
            double penaltyWeigth, ScoreType scoreType, BigDecimal minPercPlayed) throws IxigoException;
    
    /**
     * It will return the Users Average scored calculated using the most recent
     * "gamesCounter" games
     * 
     * @param gamesCounter
     * @param usersIDs
     * @return
     * @throws MarcoException
     */
    public Mono<Map<String, SvcUserAvgScore>> getUsersAvgStatsForLastXGames(Integer gamesCounter, List<String> usersIDs,
            ScoreType partionByScore, BigDecimal minPercPlayed) throws IxigoException;
}
