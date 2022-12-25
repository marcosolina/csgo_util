package com.ixigo.playersmanagercontract.models.rest;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

public class RestTeam  implements Serializable {
	private static final long serialVersionUID = 1L;
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
