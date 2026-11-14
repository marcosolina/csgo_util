package com.ixigo.playersmanagercontract.models.rest;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

public class RestTeams  implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@JsonProperty("teams")
	@ApiModelProperty(notes = "List of calculated teams used the average users scores")
    private List<RestTeam> teams;

    public List<RestTeam> getTeams() {
        return teams;
    }

    public void setTeams(List<RestTeam> teams) {
        this.teams = teams;
    }
}
