package com.marco.ixigo.discordbot.model.discord;

import java.util.List;

import com.marco.ixigo.discordbot.model.Player;

public class SavePlayersMapping {
    private List<Player> players;

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }
}
