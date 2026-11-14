package com.ixigo.demmanager.models.svc.charts;

import java.math.BigDecimal;

public class SvcUserAvgScorePerMap {
	private String steamId;
	private String mapName;
	private BigDecimal avgScore;

	public String getSteamId() {
		return steamId;
	}

	public void setSteamId(String steamId) {
		this.steamId = steamId;
	}

	public String getMapName() {
		return mapName;
	}

	public void setMapName(String mapName) {
		this.mapName = mapName;
	}

	public BigDecimal getAvgScore() {
		return avgScore;
	}

	public void setAvgScore(BigDecimal avgScore) {
		this.avgScore = avgScore;
	}

}