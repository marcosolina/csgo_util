package com.ixigo.demmanager.models.rest.demdata;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RestMapStats {
	private String mapName;
	private LocalDateTime playedOn;
	private List<RestUserGotvScore> usersStats;

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

	public List<RestUserGotvScore> getUsersStats() {
		return usersStats;
	}

	public void setUsersStats(List<RestUserGotvScore> usersStats) {
		this.usersStats = usersStats;
	}

	public boolean addUserMapStats(RestUserGotvScore ums) {
		if (usersStats == null) {
			usersStats = new ArrayList<>();
		}
		return usersStats.add(ums);
	}
}
