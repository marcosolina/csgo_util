package com.marco.csgoutil.roundparser.model.rest;

import java.time.LocalDateTime;
import java.util.List;

import com.marco.utils.http.MarcoResponse;

import io.swagger.annotations.ApiModelProperty;

/**
 * REST Model used to retrieve the list of available games stored in the system
 * 
 * @author Marco
 *
 */
public class AvailableGames extends MarcoResponse {

	@ApiModelProperty(example = "[ \"2021-01-18T19:46:39\", \"2021-01-19T19:46:39\" ]", notes = "Date as YYYY-MM-DDTHH:MM:SS")
	private List<LocalDateTime> availableGames;

	public List<LocalDateTime> getAvailableGames() {
		return availableGames;
	}

	public void setAvailableGames(List<LocalDateTime> availableGames) {
		this.availableGames = availableGames;
	}

}
