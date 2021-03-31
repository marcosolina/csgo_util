package com.marco.csgoutil.roundparser.model.rest;

import java.util.Map;

import com.marco.utils.http.MarcoResponse;

import io.swagger.annotations.ApiModelProperty;

/**
 * REST Model used to multiple Users AVG scores
 * 
 * @author Marco
 *
 */
public class UserSvgScores extends MarcoResponse {
	@ApiModelProperty(notes = "Map of users and their average score")
	private Map<String, UserAvgScore> avgScores;

	public Map<String, UserAvgScore> getAvgScores() {
		return avgScores;
	}

	public void setAvgScores(Map<String, UserAvgScore> avgScores) {
		this.avgScores = avgScores;
	}

}
