package com.ixigo.playersmanagercontract.models.rest;

import java.io.Serializable;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Response model used to return the score type to use when partition the teams
 * 
 * @author Marco
 *
 */
public class RestScoreTypes implements Serializable {
	private static final long serialVersionUID = 1L;
	@JsonProperty("types")
	private Map<String, String> types;

	public Map<String, String> getTypes() {
		return types;
	}

	public void setTypes(Map<String, String> types) {
		this.types = types;
	}
}
