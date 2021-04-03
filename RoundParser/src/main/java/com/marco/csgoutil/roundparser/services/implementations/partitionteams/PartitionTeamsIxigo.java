package com.marco.csgoutil.roundparser.services.implementations.partitionteams;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.marco.csgoutil.roundparser.model.rest.UserAvgScore;
import com.marco.csgoutil.roundparser.model.service.Team;
import com.marco.csgoutil.roundparser.partitionlibrary.PartitionTwoTeams;
import com.marco.csgoutil.roundparser.partitionlibrary.Subset;
import com.marco.csgoutil.roundparser.services.interfaces.PartitionTeams;

public class PartitionTeamsIxigo implements PartitionTeams {

	private static final Logger _LOGGER = LoggerFactory.getLogger(PartitionTeamsIxigo.class);

	@Override
	public List<Team> partitionTheUsersComparingTheScores(List<UserAvgScore> usersList, Integer partions,
			double penaltyWeight) {

		usersList.sort((o1, o2) -> o1.getAvgScore().compareTo(o2.getAvgScore()) * -1);
		Map<Integer, UserAvgScore> userMap = new HashMap<>();

		StringBuilder sbScores = new StringBuilder();
		StringBuilder sbIndex = new StringBuilder();
		sbScores.append("Scores:  ");
		sbIndex.append("Indexes: ");
		int i = 1;
		for (UserAvgScore userAvgScore : usersList) {
			userMap.put(i, userAvgScore);

			sbScores.append(userAvgScore.getAvgScore());
			sbScores.append(" ");
			sbIndex.append("  " + i + "   ");
			i++;
		}

		List<Double> scores = usersList.stream().map(u -> u.getAvgScore().doubleValue()).collect(Collectors.toList());

		PartitionTwoTeams ep = new PartitionTwoTeams(scores, penaltyWeight);
		if (_LOGGER.isDebugEnabled()) {
			_LOGGER.debug(sbScores.toString());
			_LOGGER.debug(sbIndex.toString());
			ep.print(true, 0);
		}

		List<Subset> subsetsList = ep.getSubsets();
		List<Team> listTeams = new ArrayList<>();

		subsetsList.forEach(s -> {
			Team t = new Team();
			t.setTeamScore(BigDecimal.valueOf(s.getSumVal()).setScale(2, RoundingMode.DOWN));
			s.getNumbIDs().forEach(numId -> t.addMember(userMap.get(numId)));
			listTeams.add(t);
		});

		return listTeams;
	}

	@Override
	public List<Team> partitionTheUsersComparingTheScoresAndTeamMembers(List<UserAvgScore> usersList, Integer partions,
			double penaltyWeight) {

		return partitionTheUsersComparingTheScores(usersList, partions, penaltyWeight);

	}

}
