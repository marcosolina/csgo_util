package com.marco.csgoutil.roundparser.model.rest.players;

import java.util.List;

import com.marco.csgoutil.roundparser.model.service.MapStats;
import com.marco.utils.http.MarcoResponse;

import io.swagger.annotations.ApiModelProperty;

/**
 * REST Model used to return the User scores per game/map played
 * 
 * @author Marco
 *
 */
public class UserScores extends MarcoResponse {
	@ApiModelProperty(notes = "List of single user scores per map/game")
	private List<MapStats> mapsScores;

	public List<MapStats> getMapsScores() {
		return mapsScores;
	}

	public void setMapsScores(List<MapStats> mapsScores) {
		this.mapsScores = mapsScores;
	}

}