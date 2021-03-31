package com.marco.csgoutil.roundparser.model.rest;

import java.math.BigDecimal;

public class UserAvgScore {
	private String userName;
	private String steamID;
	private BigDecimal avgScore;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getSteamID() {
		return steamID;
	}

	public void setSteamID(String steamID) {
		this.steamID = steamID;
	}

	public BigDecimal getAvgScore() {
		return avgScore;
	}

	public void setAvgScore(BigDecimal avgScore) {
		this.avgScore = avgScore;
	}

}
