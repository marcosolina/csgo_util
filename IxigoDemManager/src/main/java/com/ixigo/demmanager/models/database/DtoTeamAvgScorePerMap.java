package com.ixigo.demmanager.models.database;

public class DtoTeamAvgScorePerMap {
	private String teamName;
	private String mapName;
	private Double avgScore;

	public DtoTeamAvgScorePerMap(String teamName, String mapName, Double avgScore) {
		super();
		this.teamName = teamName;
		this.mapName = mapName;
		this.avgScore = avgScore;
	}

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

	public Double getAvgScore() {
		return avgScore;
	}

	public void setAvgScore(Double avgScore) {
		this.avgScore = avgScore;
	}
}
