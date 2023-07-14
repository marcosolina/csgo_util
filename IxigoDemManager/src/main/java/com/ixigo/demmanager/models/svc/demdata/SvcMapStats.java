package com.ixigo.demmanager.models.svc.demdata;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.ixigo.demmanager.models.svc.demdata.nodejs.SvcPlayerStats;

/**
 * Service layer model which contains the data extracted from a single DEM file
 * 
 * @author marco
 *
 */
public class SvcMapStats {
	private String mapName;
	private LocalDateTime playedOn;
	private List<SvcPlayerStats> usersStats;

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

	public List<SvcPlayerStats> getUsersStats() {
		return usersStats;
	}

	public void setUsersStats(List<SvcPlayerStats> usersStats) {
		this.usersStats = usersStats;
	}

	public boolean addUserMapStats(SvcPlayerStats ums) {
		if (usersStats == null) {
			usersStats = new ArrayList<>();
		}
		return usersStats.add(ums);
	}
}
