package com.marco.csgoutil.roundparser.services.implementations.partitionteams;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.stream.Collectors;

import com.marco.csgoutil.roundparser.model.rest.UserAvgScore;
import com.marco.csgoutil.roundparser.model.service.Team;
import com.marco.csgoutil.roundparser.services.interfaces.PartitionTeams;

/**
 * My way to partion the teams
 * 
 * @author Marco
 *
 */
public class PartitionTeamsMarco implements PartitionTeams {

	@Override
	public List<Team> partitionTheUsersComparingTheScores(List<UserAvgScore> usersList, Integer partions) {
		return partionUsers(usersList, partions).stream().collect(Collectors.toList());
	}

	/**
	 * Private method used to solve the
	 * <a href="https://en.wikipedia.org/wiki/Partition_problem">Partition
	 * Problem</a>
	 * 
	 * @param usersList    -> The whole list of users
	 * @param teamsCounter -> How many teams to create
	 * @return
	 */
	private Collection<Team> partionUsers(List<UserAvgScore> usersList, Integer teamsCounter) {

		// Reverse order
		Collections.sort(usersList, (o1, o2) -> o1.getAvgScore().compareTo(o2.getAvgScore()) * -1);

		// Creating utils objects
		Queue<UserAvgScore> userQueue = new ArrayDeque<>(usersList);
		PartitionComparator pComparator = new PartitionComparator();
		PriorityQueue<Team> partitionPriorityQueue = new PriorityQueue<>(teamsCounter, pComparator);

		// Creating the empty teams
		for (int i = 0; i < teamsCounter; i++) {
			partitionPriorityQueue.add(new Team());
		}

		// Filling the teams
		while (!userQueue.isEmpty()) {
			// Get the next user with the lowest avg score
			UserAvgScore user = userQueue.poll();

			// Get the team with the lowest score
			Team lowestSumPartition = partitionPriorityQueue.poll();

			// Add the user and update the team score
			lowestSumPartition.addMember(user);
			lowestSumPartition.increaseSum(user.getAvgScore());

			// Put the team back into the queue as the "poll" removes it
			partitionPriorityQueue.add(lowestSumPartition);
		}

		return partitionPriorityQueue;
	}

	/**
	 * Comparing the @{Team} by the team score
	 * 
	 * @author Marco
	 *
	 */
	private class PartitionComparator implements Comparator<Team> {

		@Override
		public int compare(Team o1, Team o2) {
			return o1.getTeamScore().compareTo(o2.getTeamScore());
		}

	}

	@Override
	public List<Team> partitionTheUsersComparingTheScoresAndTeamMembers(List<UserAvgScore> usersList,
			Integer partions) {
		// TODO Auto-generated method stub
		return null;
	}

}
