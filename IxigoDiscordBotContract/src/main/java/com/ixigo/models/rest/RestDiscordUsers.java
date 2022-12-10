package com.ixigo.models.rest;

import java.io.Serializable;
import java.util.List;

public class RestDiscordUsers implements Serializable {
	private static final long serialVersionUID = 1L;
	private List<RestDiscordUser> members;

	public List<RestDiscordUser> getMembers() {
		return members;
	}

	public void setMembers(List<RestDiscordUser> members) {
		this.members = members;
	}

}
