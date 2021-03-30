package com.marco.csgoutil.roundparser.model.rest;

import java.math.BigDecimal;
import java.util.Map;

import com.marco.utils.http.MarcoResponse;

public class SingleMapUserScores extends MarcoResponse {
	private Map<String, BigDecimal> usersScore;

	public Map<String, BigDecimal> getUsersScore() {
		return usersScore;
	}

	public void setUsersScore(Map<String, BigDecimal> usersScore) {
		this.usersScore = usersScore;
	}
	
}
