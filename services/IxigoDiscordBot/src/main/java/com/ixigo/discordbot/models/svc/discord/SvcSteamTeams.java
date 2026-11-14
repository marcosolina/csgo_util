package com.ixigo.discordbot.models.svc.discord;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SvcSteamTeams {
	@JsonProperty("terrorist_team")
	private List<SvcSteamUser> terrorists;
	@JsonProperty("ct_team")
	private List<SvcSteamUser> ct;

	public List<SvcSteamUser> getTerrorists() {
		return terrorists;
	}

	public void setTerrorists(List<SvcSteamUser> terrorists) {
		this.terrorists = terrorists;
	}

	public List<SvcSteamUser> getCt() {
		return ct;
	}

	public void setCt(List<SvcSteamUser> ct) {
		this.ct = ct;
	}

}
