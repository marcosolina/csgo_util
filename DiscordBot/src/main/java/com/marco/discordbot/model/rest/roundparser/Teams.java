package com.marco.discordbot.model.rest.roundparser;

import java.util.List;

public class Teams {
    private List<Team> teams;

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

    @Override
    public String toString() {
        return "Teams [teams=" + teams + "]";
    }

}
