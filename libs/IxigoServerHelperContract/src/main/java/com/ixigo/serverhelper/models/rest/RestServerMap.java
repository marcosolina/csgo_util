package com.ixigo.serverhelper.models.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RestServerMap {
	@JsonProperty("map_name")
	private String mapName;
	@JsonProperty("is_workshop_map")
	private boolean isWorkshop;
	@JsonProperty("workshop_id")
	private String workshopId;

	public String getMapName() {
		return mapName;
	}

	public void setMapName(String mapName) {
		this.mapName = mapName;
	}

	@JsonProperty(value="is_workshop_map")
	public boolean getWorkshop() {
		return isWorkshop;
	}

	public void setWorkshop(boolean isWorkshop) {
		this.isWorkshop = isWorkshop;
	}

	public String getWorkshopId() {
		return workshopId;
	}

	public void setWorkshopId(String workshopId) {
		this.workshopId = workshopId;
	}
}
