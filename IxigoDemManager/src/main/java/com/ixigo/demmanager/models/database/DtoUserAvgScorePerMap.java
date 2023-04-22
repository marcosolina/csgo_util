package com.ixigo.demmanager.models.database;

public class DtoUserAvgScorePerMap {
	private String steamId;
	private String mapName;
	private Double avgScore;
	
	public DtoUserAvgScorePerMap(String steamId, String mapName, Double avgScore) {
		super();
		this.steamId = steamId;
		this.mapName = mapName;
		this.avgScore = avgScore;
	}
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
	public Double getAvgScore() {
		return avgScore;
	}
	public void setAvgScore(Double avgScore) {
		this.avgScore = avgScore;
	}
	
	
}
