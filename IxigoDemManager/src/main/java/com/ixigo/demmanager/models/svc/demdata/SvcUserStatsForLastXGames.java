package com.ixigo.demmanager.models.svc.demdata;

import java.util.List;

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
