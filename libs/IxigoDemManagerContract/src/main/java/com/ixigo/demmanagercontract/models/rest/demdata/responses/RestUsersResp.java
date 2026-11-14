package com.ixigo.demmanagercontract.models.rest.demdata.responses;

import java.io.Serializable;
import java.util.List;

import com.ixigo.demmanagercontract.models.rest.demdata.data.RestUsers;

public class RestUsersResp implements Serializable {
	private static final long serialVersionUID = 1L;
	private List<RestUsers> users;

	public List<RestUsers> getUsers() {
		return users;
	}

	public void setUsers(List<RestUsers> users) {
		this.users = users;
	}

}
