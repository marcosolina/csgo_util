package com.ixigo.models.rest;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RestDiscordUser implements Serializable{
	private static final long serialVersionUID = 1L;
	@JsonProperty("discord_id")
	private String id;
	@JsonProperty("discord_name")
	private String name;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
