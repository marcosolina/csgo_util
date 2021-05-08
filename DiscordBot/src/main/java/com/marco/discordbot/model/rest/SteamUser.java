package com.marco.discordbot.model.rest;

public class SteamUser {
    private String steamId;
    private String userName;

    public SteamUser() {
    }

    public SteamUser(String steamId, String userName) {
        super();
        this.steamId = steamId;
        this.userName = userName;
    }

    public String getSteamId() {
        return steamId;
    }

    public void setSteamId(String steamId) {
        this.steamId = steamId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}
