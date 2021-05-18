package com.marco.discordbot.model.rest.roundparser;

import io.swagger.annotations.ApiModelProperty;

/**
 * REST Response model used to return the User Definition
 * 
 * @author Marco
 *
 */
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
