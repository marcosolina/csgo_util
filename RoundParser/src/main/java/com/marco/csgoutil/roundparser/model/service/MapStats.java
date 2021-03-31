package com.marco.csgoutil.roundparser.model.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MapStats {
	private String mapName;
	private LocalDateTime playedOn;
	private List<UserMapStats> usersStats;

	public String getMapName() {
		return mapName;
	}

	public void setMapName(String mapName) {
		this.mapName = mapName;
	}

	public LocalDateTime getPlayedOn() {
		return playedOn;
	}

	public void setPlayedOn(LocalDateTime playedOn) {
		this.playedOn = playedOn;
	}

	public List<UserMapStats> getUsersStats() {
		return usersStats;
	}

	public void setUsersStats(List<UserMapStats> usersStats) {
		this.usersStats = usersStats;
	}
	
	public boolean addUserMapStats(UserMapStats ums) {
		if(usersStats == null) {
			usersStats = new ArrayList<>();
		}
		return usersStats.add(ums);
	}

}
