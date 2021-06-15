package com.marco.ixigo.discordbot.model.playersmanager;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

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
