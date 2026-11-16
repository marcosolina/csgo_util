package com.ixigo.demmanager.models.svc.charts;

import java.math.BigDecimal;

public class SvcTeamAvgScorePerMap {
	private String teamName;
	private String mapName;
	private BigDecimal avgScore;

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
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