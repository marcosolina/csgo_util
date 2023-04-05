package com.ixigo.demmanager.models.svc.demdata;

import java.util.List;

/**
 * Service layer model which has the statistic extracted from the DEM file for a
 * specific Steam Player
 * 
 * @author marco
 *
 */
public class SvcUserStatsForLastXGames {
	private String steamId;
	private List<SvcMapStats> stats;

	public String getSteamId() {
		return steamId;
	}

	public void setSteamId(String steamId) {
		this.steamId = steamId;
	}

	public List<SvcMapStats> getStats() {
		return stats;
	}

	public void setStats(List<SvcMapStats> stats) {
		this.stats = stats;
	}

}
