package com.marco.csgoutil.roundparser.model.rest;

import java.util.List;
import java.util.Map;

import com.marco.csgoutil.roundparser.model.service.MapStats;
import com.marco.utils.http.MarcoResponse;

public class UsersScores extends MarcoResponse {
	private Map<String, List<MapStats>> usersScores;

	public Map<String, List<MapStats>> getUsersScores() {
		return usersScores;
	}

	public void setUsersScores(Map<String, List<MapStats>> usersScores) {
		this.usersScores = usersScores;
	}

}
