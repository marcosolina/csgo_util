package com.ixigo.playersmanager.services.interfaces;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.ixigo.library.errors.IxigoException;
import com.ixigo.playersmanager.models.svc.SvcTeam;
import com.ixigo.playersmanager.models.svc.SvcUserAvgScore;
import com.ixigo.playersmanagercontract.enums.ScoreType;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PlayersManager {
    /**
     * It will generate two teams and add some penalty to make even teams
     * 
     * @param numberOfMatches
     * @param usersIDs
     * @param penaltyWeigth
     * @param scoreType
     * @param minPercPlayed
     * @return
     * @throws IxigoException
     */
    public Flux<SvcTeam> generateTwoTeamsForcingSimilarTeamSizes(Integer numberOfMatches, List<String> usersIDs,
            double penaltyWeigth, ScoreType scoreType, BigDecimal minPercPlayed) throws IxigoException;

    /**
     * It will return the Users Average scored calculated using the most recent
     * "numberOfMatches"
     * 
     * @param numberOfMatches
     * @param usersIDs
     * @param partionByScore
     * @param minPercPlayed
     * @return
     * @throws IxigoException
     */
    public Mono<Map<String, SvcUserAvgScore>> getUsersAvgStatsForLastXGames(Integer numberOfMatches, List<String> usersIDs,
            ScoreType partionByScore, BigDecimal minPercPlayed) throws IxigoException;
}
