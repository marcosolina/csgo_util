package com.ixigo.playersmanager.models.svc;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class SvcTeam {
    private BigDecimal teamScore = BigDecimal.ZERO.setScale(2, RoundingMode.DOWN);
    private List<SvcUserAvgScore> members = new ArrayList<>();

    public BigDecimal getTeamScore() {
        return teamScore;
    }

    public void increaseSum(BigDecimal amount) {
        this.teamScore = this.teamScore.add(amount).setScale(2, RoundingMode.DOWN);
    }

    public void setTeamScore(BigDecimal teamScore) {
        this.teamScore = teamScore;
    }

    public List<SvcUserAvgScore> getMembers() {
        return members;
    }

    public void setMembers(List<SvcUserAvgScore> members) {
        this.members = members;
    }

    public boolean addMember(SvcUserAvgScore user) {
        return this.members.add(user);
    }
}
