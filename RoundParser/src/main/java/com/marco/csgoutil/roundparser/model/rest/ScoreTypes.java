package com.marco.csgoutil.roundparser.model.rest;

import java.util.Map;

import com.marco.utils.http.MarcoResponse;

/**
 * Response model used to return the score type to use when
 * partition the teams
 * 
 * @author Marco
 *
 */
public class ScoreTypes extends MarcoResponse {
	private Map<String, String> types;

	public Map<String, String> getTypes() {
		return types;
	}

	public void setTypes(Map<String, String> types) {
		this.types = types;
	}

}
