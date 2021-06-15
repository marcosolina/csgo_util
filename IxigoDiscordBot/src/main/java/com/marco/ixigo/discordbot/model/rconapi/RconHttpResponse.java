package com.marco.ixigo.discordbot.model.rconapi;

import io.swagger.annotations.ApiModelProperty;

public class RconHttpResponse {
    @ApiModelProperty(notes = "The RCON response (if any)", required = false)
    private String rconResponse;

    public String getRconResponse() {
        return rconResponse;
    }

    public void setRconResponse(String rconResponse) {
        this.rconResponse = rconResponse;
    }

}
