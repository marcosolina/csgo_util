package com.ixigo.demmanagercontract.models.rest.demdata;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RestMapStats  implements Serializable {
	private static final long serialVersionUID = 1L;
	@JsonProperty("map_name")
	private String mapName;
	@JsonProperty("played_on")
	private LocalDateTime playedOn;
	@JsonProperty("users_statistics")
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
