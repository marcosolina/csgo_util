package com.ixigo.discordbot.models.svc.discord;

import com.ixigo.demmanagercontract.models.rest.demdata.RestUser;

public class SvcPlayer {
	private SvcDiscordUser discordDetails;
	private RestUser steamDetails;

	public SvcPlayer() {
	}

	public SvcPlayer(SvcDiscordUser discordDetails, RestUser steamDetails) {
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

	public RestUser getSteamDetails() {
		return steamDetails;
	}

	public void setSteamDetails(RestUser steamDetails) {
		this.steamDetails = steamDetails;
	}

}
