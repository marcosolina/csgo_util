package com.marco.ixigo.discordbot.model.csgogame;

import io.swagger.annotations.ApiModelProperty;

public class User {
    @ApiModelProperty(notes = "Steam ID of the user")
    private String steamId;

    public String getSteamId() {
        return steamId;
    }

    public void setSteamId(String steamId) {
        this.steamId = steamId;
    }

}
