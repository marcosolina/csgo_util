package com.ixigo.serverhelper.models.mapgroups;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GameModes implements Serializable{
	private static final long serialVersionUID = 1L;
	@JsonProperty("GameModes_Server.txt")
	private Server server;
}
