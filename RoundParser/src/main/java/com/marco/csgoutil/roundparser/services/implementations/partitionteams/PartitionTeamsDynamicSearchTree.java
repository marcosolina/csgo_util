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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import com.marco.csgoutil.roundparser.model.rest.players.UserAvgScore;
import com.marco.csgoutil.roundparser.model.service.Team;
import com.marco.csgoutil.roundparser.services.interfaces.PartitionTeams;
import com.marco.utils.MarcoException;
import com.marco.utils.partitioning.EfficientPartition;
import com.marco.utils.partitioning.Subset;

/**
 * It uses a third party algorithm
 * 
 * @author Marco
 *
 */
public class PartitionTeamsDynamicSearchTree implements PartitionTeams {

	private static final Logger _LOGGER = LoggerFactory.getLogger(PartitionTeamsDynamicSearchTree.class);
	@Autowired
	private MessageSource msgSource;

	@Override
	public List<Team> partitionTheUsersComparingTheScores(List<UserAvgScore> usersList, Integer partions,
			double penaltyWeight) {

		usersList.sort((o1, o2) -> o1.getTeamSplitScore().compareTo(o2.getTeamSplitScore()) * -1);
		Map<Integer, UserAvgScore> userMap = new HashMap<>();

		StringBuilder sbScores = new StringBuilder();
		StringBuilder sbIndex = new StringBuilder();
		sbScores.append("Scores:  ");
		sbIndex.append("Indexes: ");
		int i = 1;
		for (UserAvgScore userAvgScore : usersList) {
			userMap.put(i, userAvgScore);

			sbScores.append(userAvgScore.getTeamSplitScore());
			sbScores.append(" ");
			sbIndex.append("  " + i + "   ");
			i++;
		}

		List<Double> scores = usersList.stream().map(u -> u.getTeamSplitScore().doubleValue()).collect(Collectors.toList());

		EfficientPartition ep = new EfficientPartition(scores, partions);
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
			double penaltyWeight) throws MarcoException {

		if (penaltyWeight == 0) {
			throw new MarcoException(msgSource.getMessage("DEMP00002", null, LocaleContextHolder.getLocale()));
		}
		if (penaltyWeight < 0) {
			throw new MarcoException(msgSource.getMessage("DEMP00003", null, LocaleContextHolder.getLocale()));
		}
		

		List<Team> teams = null;
		boolean ok = false;

		do {
			ok = true;
			teams = partitionTheUsersComparingTheScores(usersList, partions, penaltyWeight);
			teams.sort((o1, o2) -> o1.getMembers().size() < o2.getMembers().size() ? -1 : 1);

			outerloop: for (int i = 0; i < teams.size(); i++) {
				for (int j = i + 1; j < teams.size(); j++) {
					int delta = teams.get(i).getMembers().size() - teams.get(j).getMembers().size();
					if (Math.abs(delta) > 1) {
						usersList.sort((o1, o2) -> o1.getTeamSplitScore().compareTo(o2.getTeamSplitScore()));
						usersList.get(0).setTeamSplitScore(usersList.get(0).getTeamSplitScore()
								.add(BigDecimal.valueOf(penaltyWeight)).setScale(2, RoundingMode.DOWN));
						ok = false;
						break outerloop;
					}
				}
			}
		} while (!ok);

		return teams;
	}

}
