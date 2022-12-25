package com.ixigo.models.rest;

import java.io.Serializable;

public class RestPlayer  implements Serializable {
	private static final long serialVersionUID = 1L;
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
