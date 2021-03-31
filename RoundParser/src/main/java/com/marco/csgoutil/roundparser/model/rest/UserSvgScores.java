package com.marco.csgoutil.roundparser.model.rest;

import java.util.Map;

import com.marco.csgoutil.roundparser.model.service.UserMapStats;
import com.marco.utils.http.MarcoResponse;

public class UserSvgScores extends MarcoResponse {
	private Map<String, UserAvgScore> avgScores;

	public Map<String, UserAvgScore> getAvgScores() {
		return avgScores;
	}

	public void setAvgScores(Map<String, UserAvgScore> avgScores) {
		this.avgScores = avgScores;
	}

}
