package com.marco.csgoutil.roundparser.model.rest.players;

import java.util.List;
import java.util.Map;

import com.marco.csgoutil.roundparser.model.service.MapStats;
import com.marco.utils.http.MarcoResponse;

import io.swagger.annotations.ApiModelProperty;

/**
 * REST Model used to return the Users scores per different maps/games
 * 
 * @author Marco
 *
 */
public class UsersScores extends MarcoResponse {
	@ApiModelProperty(notes = "List of Users and their scores")
	private Map<String, List<MapStats>> usersScores;

	public Map<String, List<MapStats>> getUsersScores() {
		return usersScores;
	}

	public void setUsersScores(Map<String, List<MapStats>> usersScores) {
		this.usersScores = usersScores;
	}

}
