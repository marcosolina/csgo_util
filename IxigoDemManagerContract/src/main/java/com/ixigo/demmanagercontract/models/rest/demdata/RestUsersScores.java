package com.ixigo.demmanagercontract.models.rest.demdata;

import java.util.List;
import java.util.Map;

import io.swagger.annotations.ApiModelProperty;

/**
 * REST Model used to return the Users scores per different maps/games
 * 
 * @author Marco
 *
 */
public class RestUsersScores {
	@ApiModelProperty(notes = "List of Users and their scores")
	private Map<String, List<RestMapStats>> usersScores;

	public Map<String, List<RestMapStats>> getUsersScores() {
		return usersScores;
	}

	public void setUsersScores(Map<String, List<RestMapStats>> usersScores) {
		this.usersScores = usersScores;
	}
}
