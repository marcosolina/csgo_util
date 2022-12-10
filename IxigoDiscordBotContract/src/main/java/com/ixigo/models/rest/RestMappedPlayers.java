package com.ixigo.models.rest;

import java.io.Serializable;
import java.util.List;

public class RestMappedPlayers implements Serializable {
	private static final long serialVersionUID = 1L;
	private List<RestPlayer> players;

	public RestMappedPlayers() {
	}

	public RestMappedPlayers(List<RestPlayer> players) {
		super();
		this.players = players;
	}

	public List<RestPlayer> getPlayers() {
		return players;
	}

	public void setPlayers(List<RestPlayer> players) {
		this.players = players;
	}

}
