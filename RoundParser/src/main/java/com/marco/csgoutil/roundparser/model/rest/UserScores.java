package com.marco.csgoutil.roundparser.model.rest;

import java.util.List;

import com.marco.csgoutil.roundparser.model.service.MapStats;
import com.marco.utils.http.MarcoResponse;

public class UserScores extends MarcoResponse {
	private List<MapStats> mapsScores;

	public List<MapStats> getMapsScores() {
		return mapsScores;
	}

	public void setMapsScores(List<MapStats> mapsScores) {
		this.mapsScores = mapsScores;
	}

}
