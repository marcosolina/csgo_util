package com.ixigo.rconapi.models.rest;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

public class RestRconResponse  implements Serializable {
	private static final long serialVersionUID = 1L;
	@ApiModelProperty(notes = "The RCON response (if any)", required = false)
    private String rconResponse;

    public String getRconResponse() {
        return rconResponse;
    }

    public void setRconResponse(String rconResponse) {
        this.rconResponse = rconResponse;
    }
}
