package com.ixigo.demmanager.models.svc.demdata;

public class SvcUser {
	private String steamId;
	private String userName;

	public SvcUser() {
	}

	public SvcUser(String steamId) {
		this.steamId = steamId;
		this.userName = "";
	}

	public String getSteamId() {
		return steamId;
	}

	public void setSteamId(String steamId) {
		this.steamId = steamId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}
