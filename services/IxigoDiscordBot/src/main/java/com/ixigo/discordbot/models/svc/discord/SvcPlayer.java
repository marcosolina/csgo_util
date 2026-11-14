package com.ixigo.discordbot.models.svc.discord;

import com.ixigo.demmanagercontract.models.rest.demdata.data.RestUsers;

public class SvcPlayer {
	private SvcDiscordUser discordDetails;
	private RestUsers steamDetails;

	public SvcPlayer() {
	}

	public SvcPlayer(SvcDiscordUser discordDetails, RestUsers steamDetails) {
		super();
		this.discordDetails = discordDetails;
		this.steamDetails = steamDetails;
	}

	public SvcDiscordUser getDiscordDetails() {
		return discordDetails;
	}

	public void setDiscordDetails(SvcDiscordUser discordDetails) {
		this.discordDetails = discordDetails;
	}

	public RestUsers getSteamDetails() {
		return steamDetails;
	}

	public void setSteamDetails(RestUsers steamDetails) {
		this.steamDetails = steamDetails;
	}
}
