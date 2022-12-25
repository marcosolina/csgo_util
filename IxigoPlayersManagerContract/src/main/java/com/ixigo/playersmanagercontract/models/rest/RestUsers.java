package com.ixigo.playersmanagercontract.models.rest;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

public class RestUsers  implements Serializable {
	private static final long serialVersionUID = 1L;
	@ApiModelProperty(notes = "List of users")
	private List<RestUser> users;

	public List<RestUser> getUsers() {
		return users;
	}

	public void setUsers(List<RestUser> users) {
		this.users = users;
	}

}
