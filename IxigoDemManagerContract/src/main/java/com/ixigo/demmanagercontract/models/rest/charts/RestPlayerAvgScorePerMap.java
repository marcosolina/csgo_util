package com.ixigo.demmanagercontract.models.rest.charts;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Average score per map.
 * 
 * @author marco
 *
 */
public class RestPlayerAvgScorePerMap implements Serializable {
	private static final long serialVersionUID = 1L;
	@JsonProperty("steam_id")
	private String steamId;
	@JsonProperty("map_name")
	private String mapName;
	@JsonProperty("avg_score")
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
