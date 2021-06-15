package com.marco.ixigo.discordbot.model;

import com.marco.ixigo.discordbot.model.demmanager.User;
import com.marco.ixigo.discordbot.model.discord.DiscordUser;

public class Player {
    private DiscordUser discordDetails;
    private User steamDetails;

    public Player() {

    }

    public Player(DiscordUser discordDetails, User steamDetails) {
        super();
        this.discordDetails = discordDetails;
        this.steamDetails = steamDetails;
    }

    public DiscordUser getDiscordDetails() {
        return discordDetails;
    }

    public void setDiscordDetails(DiscordUser discordDetails) {
        this.discordDetails = discordDetails;
    }

    public User getSteamDetails() {
        return steamDetails;
    }

    public void setSteamDetails(User steamDetails) {
        this.steamDetails = steamDetails;
    }

}
