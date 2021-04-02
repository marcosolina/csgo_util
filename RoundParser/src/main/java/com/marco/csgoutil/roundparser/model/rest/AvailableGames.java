package com.marco.csgoutil.roundparser.model.rest;

import java.time.LocalDateTime;
import java.util.List;

import com.marco.utils.http.MarcoResponse;

/**
 * REST Model used to retrieve the list of available games stored in the system
 * 
 * @author Marco
 *
 */
public class AvailableGames extends MarcoResponse {

	private List<LocalDateTime> availableGames;

	public List<LocalDateTime> getAvailableGames() {
		return availableGames;
	}

	public void setAvailableGames(List<LocalDateTime> availableGames) {
		this.availableGames = availableGames;
	}

}
