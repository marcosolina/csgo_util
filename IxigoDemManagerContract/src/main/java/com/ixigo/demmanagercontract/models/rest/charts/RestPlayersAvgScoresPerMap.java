package com.ixigo.demmanagercontract.models.rest.charts;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * List of average scores
 * 
 * @author marco
 *
 */
public class RestPlayersAvgScoresPerMap implements Serializable {
	private static final long serialVersionUID = 1L;
	@JsonProperty("scores")
	private Map<String, List<RestPlayerAvgScorePerMap>> scores;

	public Map<String, List<RestPlayerAvgScorePerMap>> getScores() {
		return scores;
	}

	public void setScores(Map<String, List<RestPlayerAvgScorePerMap>> scores) {
		this.scores = scores;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
