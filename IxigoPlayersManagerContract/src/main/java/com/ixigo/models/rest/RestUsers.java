package com.ixigo.models.rest;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;

public class RestUsers {
	@ApiModelProperty(notes = "List of users")
	private List<RestUser> users;

	public List<RestUser> getUsers() {
		return users;
	}

	public void setUsers(List<RestUser> users) {
		this.users = users;
	}

}
