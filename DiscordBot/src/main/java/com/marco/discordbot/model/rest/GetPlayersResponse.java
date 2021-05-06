package com.marco.discordbot.model.rest;

import java.util.List;

import com.marco.utils.http.MarcoResponse;

public class GetPlayersResponse extends MarcoResponse {
    private List<Player> players;

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

}
