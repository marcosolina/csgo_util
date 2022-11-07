package com.ixigo.playersmanager.services.interfaces;

import java.util.List;

import com.ixigo.library.errors.IxigoException;
import com.ixigo.playersmanager.models.svc.SvcTeam;
import com.ixigo.playersmanager.models.svc.SvcUserAvgScore;

import reactor.core.publisher.Flux;

/**
 * It provides the logic to partion the teams
 * 
 * @author Marco
 *
 */
public interface PartitionTeams {
    /**
     * It will partition the teams considering only the scores. This means that you
     * can have one team with 2 players and the other one with 7 players because the
     * "team scores" is equal
     * 
     * @param usersList
     * @param partions
     * @return
     */
    public Flux<SvcTeam> partitionTheUsersComparingTheScores(List<SvcUserAvgScore> usersList, Integer partions,
            double penaltyWeight) throws IxigoException;

    /**
     * It will try to create teams with even number of players adding some penalty
     * if required
     * 
     * @param usersList
     * @param partions
     * @param penaltyWeight
     * @return
     */
    public Flux<SvcTeam> partitionTheUsersComparingTheScoresAndTeamMembers(List<SvcUserAvgScore> usersList, Integer partions,
            double penaltyWeight) throws IxigoException;
}
