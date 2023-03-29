package com.ixigo.serverhelper.models.rest;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

public class RestServerMaps implements Serializable {
	private static final long serialVersionUID = 1L;
	@JsonProperty("server_maps")
	@ApiModelProperty(notes = "The maps available on the server", required = false)
	private List<RestServerMap> maps;

	public List<RestServerMap> getMaps() {
		return maps;
	}

	public void setMaps(List<RestServerMap> maps) {
		this.maps = maps;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
