package com.marco.csgoutil.roundparser.model.rest;

import java.util.List;

import com.marco.csgoutil.roundparser.model.service.MapStats;
import com.marco.utils.http.MarcoResponse;

public class MapsScores extends MarcoResponse {
	private List<MapStats> mapScores;

	public List<MapStats> getMapScores() {
		return mapScores;
	}

	public void setMapScores(List<MapStats> mapScores) {
		this.mapScores = mapScores;
	}

}
