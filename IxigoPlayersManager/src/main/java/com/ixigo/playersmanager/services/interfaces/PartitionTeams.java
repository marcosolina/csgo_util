package com.ixigo.playersmanager.services.interfaces;

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
     * It will try to create two teams with even number of players adding some penalty
     * if required
     * 
     * @param usersList
     * @param partions
     * @param penaltyWeight
     * @return
     */
    public Flux<SvcTeam> partitionTheUsersIntoTwoTeams(Flux<SvcUserAvgScore> usersList, double penaltyWeight) throws IxigoException;
}
