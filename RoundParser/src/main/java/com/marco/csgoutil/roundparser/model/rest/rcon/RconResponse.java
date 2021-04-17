package com.marco.csgoutil.roundparser.model.rest.rcon;

import com.marco.utils.http.MarcoResponse;

public class RconResponse extends MarcoResponse {
	private String rconResponse;

	public String getRconResponse() {
		return rconResponse;
	}

	public void setRconResponse(String rconResponse) {
		this.rconResponse = rconResponse;
	}
}
