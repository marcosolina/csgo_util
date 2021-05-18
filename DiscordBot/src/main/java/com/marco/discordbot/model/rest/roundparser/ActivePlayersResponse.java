package com.marco.discordbot.model.rest.roundparser;

import java.util.List;
import java.util.Map;

import com.marco.discordbot.enums.TeamType;
import com.marco.utils.http.MarcoResponse;

public class ActivePlayersResponse extends MarcoResponse {
    private Map<TeamType, List<User>> players;

    public Map<TeamType, List<User>> getPlayers() {
        return players;
    }

    public void setPlayers(Map<TeamType, List<User>> players) {
        this.players = players;
    }

}
