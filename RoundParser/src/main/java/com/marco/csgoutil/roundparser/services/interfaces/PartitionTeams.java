package com.marco.csgoutil.roundparser.services.interfaces;

import java.util.List;

import com.marco.csgoutil.roundparser.model.rest.players.UserAvgScore;
import com.marco.csgoutil.roundparser.model.service.Team;
import com.marco.utils.MarcoException;

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
	public List<Team> partitionTheUsersComparingTheScores(List<UserAvgScore> usersList, Integer partions,
			double penaltyWeight) throws MarcoException;

	/**
	 * Il will try to create teams with even number of players adding some penalty
	 * if required
	 * 
	 * @param usersList
	 * @param partions
	 * @param penaltyWeight
	 * @return
	 */
	public List<Team> partitionTheUsersComparingTheScoresAndTeamMembers(List<UserAvgScore> usersList, Integer partions,
			double penaltyWeight) throws MarcoException;
}
