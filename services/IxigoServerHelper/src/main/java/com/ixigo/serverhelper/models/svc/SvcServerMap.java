package com.ixigo.serverhelper.models.svc;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SvcServerMap {
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

	public boolean isWorkshop() {
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
