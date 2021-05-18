package com.marco.csgoutil.roundparser.model.rest.rcon;

import java.util.List;
import java.util.Map;

import com.marco.csgoutil.roundparser.enums.TeamType;
import com.marco.csgoutil.roundparser.model.rest.players.User;
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
