package com.ixigo.demmanager.models.svc.demdata.responses;

import java.io.Serializable;
import java.util.List;

import com.ixigo.demmanager.models.svc.demdata.data.SvcPlayerMatchStatsExtended;

public class SvcUserStatsForLastXGames implements Serializable {
	private static final long serialVersionUID = 1L;
	private String steamId;
	private List<SvcPlayerMatchStatsExtended> scores;

	public String getSteamId() {
		return steamId;
	}

	public void setSteamId(String steamId) {
		this.steamId = steamId;
	}

	public List<SvcPlayerMatchStatsExtended> getScores() {
		return scores;
	}

	public void setScores(List<SvcPlayerMatchStatsExtended> scores) {
		this.scores = scores;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
