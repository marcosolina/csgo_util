package com.ixigo.playersmanagercontract.models.rest;

import java.io.Serializable;
import java.math.BigDecimal;

import io.swagger.annotations.ApiModelProperty;

public class RestUserAvgScore  implements Serializable {
	private static final long serialVersionUID = 1L;
	@ApiModelProperty(notes = "Steam ID of the user")
	private String steamID;
	@ApiModelProperty(notes = "User Name")
	private String userName;
	@ApiModelProperty(notes = "Scored used to genarate the teams")
	private BigDecimal teamSplitScore;
	@ApiModelProperty(notes = "Back-up of the original value")
	private BigDecimal originalTeamSplitScore;

	public String getSteamID() {
		return steamID;
	}

	public void setSteamID(String steamID) {
		this.steamID = steamID;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public BigDecimal getTeamSplitScore() {
		return teamSplitScore;
	}

	public void setTeamSplitScore(BigDecimal teamSplitScore) {
		this.teamSplitScore = teamSplitScore;
	}

	public BigDecimal getOriginalTeamSplitScore() {
		return originalTeamSplitScore;
	}

	public void setOriginalTeamSplitScore(BigDecimal originalTeamSplitScore) {
		this.originalTeamSplitScore = originalTeamSplitScore;
	}

}
