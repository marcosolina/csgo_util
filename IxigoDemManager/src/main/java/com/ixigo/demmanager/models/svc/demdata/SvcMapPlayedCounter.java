package com.ixigo.demmanager.models.svc.demdata;

/**
 * Service layer model which has the number of times the map was played
 * 
 * @author Marco
 *
 */
public class SvcMapPlayedCounter {
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
