package com.ixigo.demmanagercontract.models.rest.charts;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ixigo.demmanagercontract.models.rest.demdata.RestUserGotvScore;

/**
 * Scores per map
 * 
 * @author marco
 *
 */
public class RestTeamScorePerMap implements Serializable {
	private static final long serialVersionUID = 1L;
	@JsonProperty("matches")
	private Map<String, List<RestUserGotvScore>> matches = new HashMap<>();

	public Map<String, List<RestUserGotvScore>> getMatches() {
		return matches;
	}

	public void setMatches(Map<String, List<RestUserGotvScore>> matches) {
		this.matches = matches;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
