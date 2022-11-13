package com.ixigo.rconapi.models.rest;

import io.swagger.annotations.ApiModelProperty;

public class RestRconResponse {
	@ApiModelProperty(notes = "The RCON response (if any)", required = false)
    private String rconResponse;

    public String getRconResponse() {
        return rconResponse;
    }

    public void setRconResponse(String rconResponse) {
        this.rconResponse = rconResponse;
    }
}
