package com.marco.csgoutil.roundparser.services.interfaces;

import java.util.List;

import com.marco.csgoutil.roundparser.model.rest.UserAvgScore;
import com.marco.csgoutil.roundparser.model.service.Team;

public interface PartitionTeams {
	public List<Team> partitionTheUsers(List<UserAvgScore> usersList, Integer partions);
}
