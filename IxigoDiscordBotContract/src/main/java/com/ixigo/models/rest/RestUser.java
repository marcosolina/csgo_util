package com.ixigo.models.rest;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

public class RestUser implements Serializable{
	private static final long serialVersionUID = 1L;
	@JsonProperty("steam_id")
	@ApiModelProperty(notes = "Steam ID of the user")
    private String steamId;
	@JsonProperty("steam_user_name")
    @ApiModelProperty(notes = "User Name")
    private String userName;
    
    public RestUser() {}
    
    public RestUser(String steamId) {
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
