package com.ixigo.demmanager.models.svc;

/**
 * Service layer model which carries the DEM file system info
 * 
 * @author marco
 *
 */
public class SvcFileInfo {
	private String name;
	private String mapName;
	private String size;
	private String url;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getMapName() {
		return mapName;
	}

	public void setMapName(String mapName) {
		this.mapName = mapName;
	}
}
