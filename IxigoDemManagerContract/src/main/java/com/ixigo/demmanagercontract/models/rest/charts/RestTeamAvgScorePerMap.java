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
public class RestTeamAvgScorePerMap implements Serializable {
	private static final long serialVersionUID = 1L;
	@JsonProperty("team")
	private String teamName;
	@JsonProperty("map_name")
	private String mapName;
	@JsonProperty("avg_score")
	private BigDecimal avgScore;

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
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
