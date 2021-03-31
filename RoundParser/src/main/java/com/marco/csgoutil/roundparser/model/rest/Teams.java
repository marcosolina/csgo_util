package com.marco.csgoutil.roundparser.model.rest;

import java.util.List;

import com.marco.csgoutil.roundparser.model.service.Team;
import com.marco.utils.http.MarcoResponse;

public class Teams extends MarcoResponse {
	private List<Team> teams;

	public List<Team> getTeams() {
		return teams;
	}

	public void setTeams(List<Team> teams) {
		this.teams = teams;
	}

}
