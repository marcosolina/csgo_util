package com.ixigo.demmanager.models.rest;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * @author Marco
 *
 */
public class RestFileInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	@JsonProperty("name")
	private String name;
	@JsonProperty("map_name")
	private String mapName;
	@JsonProperty("size")
	private String size;
	@JsonProperty("url")
	private String url;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMapName() {
		return mapName;
	}

	public void setMapName(String mapName) {
		this.mapName = mapName;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
