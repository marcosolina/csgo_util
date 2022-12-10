package com.ixigo.models.rest;

public class RestPlayer {
	private RestDiscordUser discordDetails;
	private RestUser steamDetails;

	public RestDiscordUser getDiscordDetails() {
		return discordDetails;
	}

	public void setDiscordDetails(RestDiscordUser discordDetails) {
		this.discordDetails = discordDetails;
	}

	public RestUser getSteamDetails() {
		return steamDetails;
	}

	public void setSteamDetails(RestUser steamDetails) {
		this.steamDetails = steamDetails;
	}

}
