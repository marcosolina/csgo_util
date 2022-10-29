package com.ixigo.demmanagercontract.models.rest.demdata;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * REST Response model used to return the list of known users
 * 
 * @author Marco
 *
 */
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
