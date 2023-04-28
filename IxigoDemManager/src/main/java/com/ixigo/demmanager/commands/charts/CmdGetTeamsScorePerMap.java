package com.ixigo.demmanager.commands.charts;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ixigo.demmanagercontract.models.rest.charts.RestTeamScorePerMap;
import com.ixigo.library.mediators.web.interfaces.WebCommandRequest;

import lombok.experimental.FieldNameConstants;

/**
 * Command used to retrieve the teams score per map
 * 
 * @author marco
 *
 */
@FieldNameConstants
public class CmdGetTeamsScorePerMap implements WebCommandRequest<RestTeamScorePerMap> {
	@JsonProperty("map")
	private String map;
	@JsonProperty("matchesToConsider")
	private String matchesToConsider;

	public CmdGetTeamsScorePerMap(String map, String matchesToConsider) {
		super();
		this.map = map;
		this.matchesToConsider = matchesToConsider;
	}

	public String getMap() {
		return map;
	}

	public void setMap(String map) {
		this.map = map;
	}

	public String getMatchesToConsider() {
		return matchesToConsider;
	}

	public void setMatchesToConsider(String matchesToConsider) {
		this.matchesToConsider = matchesToConsider;
	}

}
