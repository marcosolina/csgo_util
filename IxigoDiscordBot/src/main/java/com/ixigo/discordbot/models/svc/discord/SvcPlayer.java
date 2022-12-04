package com.ixigo.discordbot.models.svc.discord;

import com.ixigo.demmanagercontract.models.rest.demdata.RestUser;

public class SvcPlayer {
	private DiscordUser discordDetails;
	private RestUser steamDetails;

	public SvcPlayer() {
	}

	public SvcPlayer(DiscordUser discordDetails, RestUser steamDetails) {
		super();
		this.discordDetails = discordDetails;
		this.steamDetails = steamDetails;
	}

	public DiscordUser getDiscordDetails() {
		return discordDetails;
	}

	public void setDiscordDetails(DiscordUser discordDetails) {
		this.discordDetails = discordDetails;
	}

	public RestUser getSteamDetails() {
		return steamDetails;
	}

	public void setSteamDetails(RestUser steamDetails) {
		this.steamDetails = steamDetails;
	}

}
