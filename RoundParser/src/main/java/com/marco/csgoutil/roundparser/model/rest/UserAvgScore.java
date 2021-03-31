package com.marco.csgoutil.roundparser.model.rest;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModelProperty;

/**
 * REST Response model used to return the Average score calculated for the
 * specific user
 * 
 * @author Marco
 *
 */
public class UserAvgScore {
	@ApiModelProperty(notes = "Steam ID of the user")
	private String steamID;
	@ApiModelProperty(notes = "User Name")
	private String userName;
	@ApiModelProperty(notes = "Average score")
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
