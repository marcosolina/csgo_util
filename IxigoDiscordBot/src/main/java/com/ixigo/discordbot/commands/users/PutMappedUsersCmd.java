package com.ixigo.discordbot.commands.users;

import com.ixigo.library.mediators.web.interfaces.WebCommandRequest;
import com.ixigo.models.rest.RestMappedPlayers;

public class PutMappedUsersCmd implements WebCommandRequest<Void> {
	private RestMappedPlayers playersMap;

	public PutMappedUsersCmd(RestMappedPlayers playersMap) {
		super();
		this.playersMap = playersMap;
	}

	public RestMappedPlayers getPlayersMap() {
		return playersMap;
	}

	public void setPlayersMap(RestMappedPlayers playersMap) {
		this.playersMap = playersMap;
	}

}
