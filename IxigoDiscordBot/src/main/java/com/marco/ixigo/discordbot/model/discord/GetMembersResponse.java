package com.marco.ixigo.discordbot.model.discord;

import java.util.List;

import com.marco.utils.http.MarcoResponse;

public class GetMembersResponse extends MarcoResponse {
    private List<DiscordUser> members;

    public List<DiscordUser> getMembers() {
        return members;
    }

    public void setMembers(List<DiscordUser> members) {
        this.members = members;
    }

}
