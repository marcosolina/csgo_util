package com.ixigo.playersmanagercontract.models.rest;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;

public class RestTeams {
	@ApiModelProperty(notes = "List of calculated teams used the average users scores")
    private List<RestTeam> teams;

    public List<RestTeam> getTeams() {
        return teams;
    }

    public void setTeams(List<RestTeam> teams) {
        this.teams = teams;
    }
}
