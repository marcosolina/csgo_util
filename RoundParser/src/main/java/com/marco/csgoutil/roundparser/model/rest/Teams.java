package com.marco.csgoutil.roundparser.model.rest;

import java.util.List;

import com.marco.csgoutil.roundparser.model.service.Team;
import com.marco.utils.http.MarcoResponse;

import io.swagger.annotations.ApiModelProperty;

/**
 * REST Response model used to return the calculated Teams
 * 
 * @author Marco
 *
 */
public class Teams extends MarcoResponse {
	@ApiModelProperty(notes = "List of calculated teams used the average users scores")
	private List<Team> teams;

	public List<Team> getTeams() {
		return teams;
	}

	public void setTeams(List<Team> teams) {
		this.teams = teams;
	}

}
