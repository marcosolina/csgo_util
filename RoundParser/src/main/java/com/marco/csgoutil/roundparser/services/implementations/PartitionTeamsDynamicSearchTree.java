package com.marco.csgoutil.roundparser.services.implementations;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.marco.csgoutil.roundparser.model.rest.UserAvgScore;
import com.marco.csgoutil.roundparser.model.service.Team;
import com.marco.csgoutil.roundparser.model.service.UserMapStats;
import com.marco.csgoutil.roundparser.partition.EffPartition;
import com.marco.csgoutil.roundparser.partition.Subset;
import com.marco.csgoutil.roundparser.services.interfaces.PartitionTeams;

/**
 * A modified version of <a href=
 * "https://www.codeproject.com/Articles/1265125/Fast-and-Practically-perfect-Partition-Problem-Sol#DST">this
 * solution</a>
 * 
 * @author Marco
 *
 */
public class PartitionTeamsDynamicSearchTree implements PartitionTeams {

	@Override
	public List<Team> partitionTheUsers(List<UserAvgScore> usersList, Integer partions) {

		usersList.sort((o1, o2) -> o1.getAvgScore().compareTo(o2.getAvgScore() )* -1);
		
		System.out.print("Scores: ");
		for (UserAvgScore userAvgScore : usersList) {
			System.out.print(userAvgScore.getAvgScore() + " ");
		}
		System.out.println("");
		System.out.print("Indexs: ");
		
		int i = 1;
		Map<Integer, UserAvgScore> userMap = new HashMap<>();
		for (UserAvgScore userAvgScore : usersList) {
			userMap.put(i, userAvgScore);
			System.out.print("  " + i++ + "   ");
		}
		System.out.println("");
		System.out.println("");
		
		List<Double> scores = usersList.stream().map(u -> u.getAvgScore().doubleValue()).collect(Collectors.toList());
		
		EffPartition ep = new EffPartition(scores, partions);
		List<Subset> subsetsList = ep.Subsets();
		ep.Print(true, 0);
		
		List<Team> listTeams = new ArrayList<>();
		
		subsetsList.forEach(s ->{
			Team t = new Team();
			t.setTeamScore(BigDecimal.valueOf(s.sum()).setScale(2, RoundingMode.DOWN));
			s.getNumbIDs().forEach(numId -> t.addMember(userMap.get(numId)));
			listTeams.add(t);
		});
		
		return listTeams;
	}

}
