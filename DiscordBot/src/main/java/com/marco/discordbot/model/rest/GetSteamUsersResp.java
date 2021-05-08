package com.marco.discordbot.model.rest;

import java.util.List;

import com.marco.utils.http.MarcoResponse;

public class GetSteamUsersResp extends MarcoResponse {
    private List<SteamUser> users;

    public List<SteamUser> getUsers() {
        return users;
    }

    public void setUsers(List<SteamUser> users) {
        this.users = users;
    }
}
