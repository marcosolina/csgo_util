package com.ixigo.demmanagercontract.models.rest.demdata;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

/**
 * REST Model used to return the Users scores per different maps/games
 * 
 * @author Marco
 *
 */
public class RestUsersScores  implements Serializable {
	private static final long serialVersionUID = 1L;
	@ApiModelProperty(notes = "List of Users and their scores")
	@JsonProperty("users_scores")
	private Map<String, List<RestMapStats>> usersScores;

	public Map<String, List<RestMapStats>> getUsersScores() {
		return usersScores;
	}

	public void setUsersScores(Map<String, List<RestMapStats>> usersScores) {
		this.usersScores = usersScores;
	}
}
