package com.marco.csgoutil.roundparser.model.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import com.marco.csgoutil.roundparser.model.rest.UserAvgScore;

import io.swagger.annotations.ApiModelProperty;

/**
 * DTO Model is used to return the infor related to the teams that were
 * "calculated"
 * 
 * @author Marco
 *
 */
public class Team {
	@ApiModelProperty(notes = "Total team score")
	private BigDecimal teamScore = BigDecimal.ZERO.setScale(2, RoundingMode.DOWN);
	@ApiModelProperty(notes = "List of the members of the team with their average score")
	private List<UserAvgScore> members = new ArrayList<>();

	public BigDecimal getTeamScore() {
		return teamScore;
	}

	public void increaseSum(BigDecimal amount) {
		this.teamScore = this.teamScore.add(amount).setScale(2, RoundingMode.DOWN);
	}

	public void setTeamScore(BigDecimal teamScore) {
		this.teamScore = teamScore;
	}

	public List<UserAvgScore> getMembers() {
		return members;
	}

	public void setMembers(List<UserAvgScore> members) {
		this.members = members;
	}

	public boolean addMember(UserAvgScore user) {
		return this.members.add(user);
	}
}
