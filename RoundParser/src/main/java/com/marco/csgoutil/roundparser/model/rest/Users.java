package com.marco.csgoutil.roundparser.model.rest;

import java.util.List;

import com.marco.utils.http.MarcoResponse;

public class Users extends MarcoResponse {
	private List<User> users;

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

}
