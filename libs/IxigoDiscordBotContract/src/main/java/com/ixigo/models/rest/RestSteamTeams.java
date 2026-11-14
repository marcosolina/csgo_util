package com.ixigo.models.rest;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RestSteamTeams implements Serializable {
	private static final long serialVersionUID = 1L;
	@JsonProperty("terrorist_team")
	private List<RestUser> terrorists;
	@JsonProperty("ct_team")
	private List<RestUser> ct;

	public List<RestUser> getTerrorists() {
		return terrorists;
	}

	public void setTerrorists(List<RestUser> terrorists) {
		this.terrorists = terrorists;
	}

	public List<RestUser> getCt() {
		return ct;
	}

	public void setCt(List<RestUser> ct) {
		this.ct = ct;
	}

}
