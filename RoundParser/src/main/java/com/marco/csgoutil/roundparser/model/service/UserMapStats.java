package com.marco.csgoutil.roundparser.model.service;

/**
 * This DTO is used to exchange info of the Single user score
 * 
 * @author Marco
 *
 */
public class UserMapStats {
	private String userName;
	private String steamID;
	private Integer score;

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

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

}
