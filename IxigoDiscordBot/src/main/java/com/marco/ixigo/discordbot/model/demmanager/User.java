package com.marco.ixigo.discordbot.model.demmanager;

import io.swagger.annotations.ApiModelProperty;

public class User {
    @ApiModelProperty(notes = "Steam ID of the user")
    private String steamId;
    @ApiModelProperty(notes = "User Name")
    private String userName;
    
    public User() {}
    
    public User(String steamId) {
        this.steamId = steamId;
        this.userName = "";
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