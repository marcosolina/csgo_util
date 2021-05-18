package com.marco.discordbot.model.rest.roundparser;

import java.util.ArrayList;
import java.util.List;

public class Team {
    private List<RoundParserUser> members = new ArrayList<>();

    public List<RoundParserUser> getMembers() {
        return members;
    }

    public void setMembers(List<RoundParserUser> members) {
        this.members = members;
    }

    @Override
    public String toString() {
        return "Team [members=" + members + "]";
    }

}
