package com.marco.csgorestapi.model;

import com.marco.utils.http.MarcoResponse;

public class RconHttpResponse extends MarcoResponse {

	private String rconResponse;

	public String getRconResponse() {
		return rconResponse;
	}

	public void setRconResponse(String rconResponse) {
		this.rconResponse = rconResponse;
	}

}
