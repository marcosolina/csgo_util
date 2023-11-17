package com.ixigo.discordbot.commands.users;

import com.ixigo.library.mediators.web.interfaces.WebCommandRequest;
import com.ixigo.models.rest.RestSteamTeams;

public class SetToVoiceChannelCmd implements WebCommandRequest<Void> {
	private RestSteamTeams teams;

	public SetToVoiceChannelCmd(RestSteamTeams teams) {
		super();
		this.teams = teams;
	}

	public RestSteamTeams getTeams() {
		return teams;
	}
	
	public void setTeams(RestSteamTeams teams) {
		this.teams = teams;
	}
}
