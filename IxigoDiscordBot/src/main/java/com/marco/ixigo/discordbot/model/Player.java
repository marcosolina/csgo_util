package com.marco.ixigo.discordbot.model;

public class Player {
    private DiscordUser discordDetails;
    private SteamUser steamDetails;

    public Player() {

    }

    public Player(DiscordUser discordDetails, SteamUser steamDetails) {
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

    public SteamUser getSteamDetails() {
        return steamDetails;
    }

    public void setSteamDetails(SteamUser steamDetails) {
        this.steamDetails = steamDetails;
    }

}
