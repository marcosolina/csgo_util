package com.ixigo.models.rest;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RestDiscordUsers implements Serializable {
	private static final long serialVersionUID = 1L;
	@JsonProperty("members")
	private List<RestDiscordUser> members;

	public RestDiscordUsers() {
	}

	public RestDiscordUsers(List<RestDiscordUser> members) {
		super();
		this.members = members;
	}

	public List<RestDiscordUser> getMembers() {
		return members;
	}

	public void setMembers(List<RestDiscordUser> members) {
		this.members = members;
	}

}
