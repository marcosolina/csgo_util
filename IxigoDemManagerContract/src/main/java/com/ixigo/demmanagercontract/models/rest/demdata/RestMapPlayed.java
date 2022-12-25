package com.ixigo.demmanagercontract.models.rest.demdata;

import java.io.Serializable;

/**
 * Number of times the map was played
 * 
 * @author Marco
 *
 */
public class RestMapPlayed implements Serializable {
	private static final long serialVersionUID = 1L;
	private String mapName;
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
