package com.ixigo.demmanagercontract.models.rest.demdata;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

/**
 * REST Response model used to return the User Definition
 * 
 * @author Marco
 *
 */
public class RestUser  implements Serializable {
	private static final long serialVersionUID = 1L;
	@ApiModelProperty(notes = "Steam ID of the user")
	@JsonProperty("steam_id")
    private String steamId;
    @ApiModelProperty(notes = "User Name")
    @JsonProperty("user_name")
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
