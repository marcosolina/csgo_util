package com.marco.discordbot.model.rest.roundparser;

import io.swagger.annotations.ApiModelProperty;

public class RoundParserUser {
    @ApiModelProperty(notes = "Steam ID of the user")
    private String steamID;
    @ApiModelProperty(notes = "User Name")
    private String userName;

    public String getSteamID() {
        return steamID;
    }

    public void setSteamID(String steamID) {
        this.steamID = steamID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "RoundParserUser [steamID=" + steamID + ", userName=" + userName + "]";
    }

}
