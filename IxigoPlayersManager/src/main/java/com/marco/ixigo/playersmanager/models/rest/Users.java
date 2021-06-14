package com.marco.ixigo.playersmanager.models.rest;

import java.util.List;

import com.marco.utils.http.MarcoResponse;

import io.swagger.annotations.ApiModelProperty;

/**
 * REST Response model used to return the list of known users
 * 
 * @author Marco
 *
 */
public class Users extends MarcoResponse {
    @ApiModelProperty(notes = "List of users")
    private List<User> users;

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

}
