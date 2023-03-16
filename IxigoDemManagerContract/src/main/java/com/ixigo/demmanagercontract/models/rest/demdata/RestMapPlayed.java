package com.ixigo.demmanagercontract.models.rest.demdata;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Number of times the map was played
 * 
 * @author Marco
 *
 */
public class RestMapPlayed implements Serializable {
	private static final long serialVersionUID = 1L;
	@JsonProperty("map_name")
	private String mapName;
	@JsonProperty("count")
	private Long count;

	public String getMapName() {
		return mapName;
	}

	public void setMapName(String mapName) {
		this.mapName = mapName;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}
}
