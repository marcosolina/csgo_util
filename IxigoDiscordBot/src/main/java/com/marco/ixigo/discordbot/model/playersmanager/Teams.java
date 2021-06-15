package com.marco.ixigo.discordbot.model.playersmanager;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;

public class Teams {
    @ApiModelProperty(notes = "List of calculated teams used the average users scores")
    private List<Team> teams;

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }
}
