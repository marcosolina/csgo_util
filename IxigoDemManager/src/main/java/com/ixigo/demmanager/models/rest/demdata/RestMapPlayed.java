package com.ixigo.demmanager.models.rest.demdata;

/**
 * Number of times the map was played
 * 
 * @author Marco
 *
 */
public class RestMapPlayed {
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
