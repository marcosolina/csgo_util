package com.marco.ixigo.discordbot.model.demmanager;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;

public class Users {
    @ApiModelProperty(notes = "List of users")
    private List<User> users;

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

}
