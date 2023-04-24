package com.ixigo.demmanager.commands.charts;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ixigo.demmanagercontract.models.rest.charts.RestAvgScoresPerMap;
import com.ixigo.library.mediators.web.interfaces.WebCommandRequest;

import lombok.experimental.FieldNameConstants;

/**
 * Command dispatched to retrieve the number of times the maps where played
 * 
 * @author marco
 *
 */
@FieldNameConstants
public class CmdGetAvgScorePerMap implements WebCommandRequest<RestAvgScoresPerMap> {
	@JsonProperty("steamIds")
	private List<String> steamIds;
	@JsonProperty("scoreType")
	private String scoreType;
	@JsonProperty("maps")
	private List<String> maps;
	@JsonProperty("matchesToConsider")
	private String matchesToConsider;

	public CmdGetAvgScorePerMap(List<String> steamIds, String scoreType, List<String> maps, String matchesToConsider) {
		super();
		this.steamIds = steamIds;
		this.scoreType = scoreType;
		this.maps = maps;
		this.matchesToConsider = matchesToConsider;
	}

	public List<String> getSteamIds() {
		return steamIds;
	}

	public void setSteamIds(List<String> steamIds) {
		this.steamIds = steamIds;
	}

	public String getScoreType() {
		return scoreType;
	}

	public void setScoreType(String scoreType) {
		this.scoreType = scoreType;
	}

	public List<String> getMaps() {
		return maps;
	}

	public void setMaps(List<String> maps) {
		this.maps = maps;
	}

	public String getMatchesToConsider() {
		return matchesToConsider;
	}

	public void setMatchesToConsider(String matchesToConsider) {
		this.matchesToConsider = matchesToConsider;
	}

}
