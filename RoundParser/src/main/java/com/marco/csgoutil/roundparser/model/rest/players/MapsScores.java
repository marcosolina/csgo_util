package com.marco.csgoutil.roundparser.model.rest.players;

import java.util.List;

import com.marco.csgoutil.roundparser.model.service.MapStats;
import com.marco.utils.http.MarcoResponse;

import io.swagger.annotations.ApiModelProperty;

/**
 * Response model used to return the new scores added into the system
 * 
 * @author Marco
 *
 */
public class MapsScores extends MarcoResponse {
	@ApiModelProperty(notes = "List of new scores added into the system")
	private List<MapStats> mapScores;

	public List<MapStats> getMapScores() {
		return mapScores;
	}

	public void setMapScores(List<MapStats> mapScores) {
		this.mapScores = mapScores;
	}

}
