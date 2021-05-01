package com.marco.csgorestapi.model.rest;

import com.marco.utils.http.MarcoResponse;

import io.swagger.annotations.ApiModelProperty;

/**
 * HTTP response model returned after forwarding the RCON command
 * 
 * @author Marco
 *
 */
public class RconHttpResponse extends MarcoResponse {

	@ApiModelProperty(notes = "The RCON response (if any)", required = false)
	private String rconResponse;

	public String getRconResponse() {
		return rconResponse;
	}

	public void setRconResponse(String rconResponse) {
		this.rconResponse = rconResponse;
	}

}
