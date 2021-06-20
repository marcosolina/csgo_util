package com.marco.ixigo.playersmanager.models.rest;

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
