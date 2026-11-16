package com.ixigo.discordbot.models.svc.discord;

public class SvcDiscordUser {
	private String id;
	private String name;

	public SvcDiscordUser() {
	}

	public SvcDiscordUser(String id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

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
