package com.ixigo.models.rest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

public class RestTeam {
	@ApiModelProperty(notes = "Total team score")
    private BigDecimal teamScore = BigDecimal.ZERO.setScale(2, RoundingMode.DOWN);
    @ApiModelProperty(notes = "List of the members of the team with their average score")
    private List<RestUserAvgScore> members = new ArrayList<>();

    public BigDecimal getTeamScore() {
        return teamScore;
    }

    public void increaseSum(BigDecimal amount) {
        this.teamScore = this.teamScore.add(amount).setScale(2, RoundingMode.DOWN);
    }

    public void setTeamScore(BigDecimal teamScore) {
        this.teamScore = teamScore;
    }

    public List<RestUserAvgScore> getMembers() {
        return members;
    }

    public void setMembers(List<RestUserAvgScore> members) {
        this.members = members;
    }

    public boolean addMember(RestUserAvgScore user) {
        return this.members.add(user);
    }
}
