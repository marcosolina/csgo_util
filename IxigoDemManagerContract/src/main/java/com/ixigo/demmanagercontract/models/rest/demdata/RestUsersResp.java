package com.ixigo.demmanagercontract.models.rest.demdata;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

/**
 * REST Response model used to return the list of known users
 * 
 * @author Marco
 *
 */
public class RestUsers  implements Serializable {
	private static final long serialVersionUID = 1L;
	@ApiModelProperty(notes = "List of users")
	@JsonProperty("users")
	private List<RestUser> users;

	public List<RestUser> getUsers() {
		return users;
	}

	public void setUsers(List<RestUser> users) {
		this.users = users;
	}
}
