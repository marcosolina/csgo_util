package com.ixigo.demmanager.models.svc.demdata.nodejs;

import java.time.LocalDateTime;

public class SvcMapFileStats {
	private LocalDateTime date;
	private String mapName;
	private String fileName;
	private Integer matchId;

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public String getMapName() {
		return mapName;
	}

	public void setMapName(String mapName) {
		this.mapName = mapName;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Integer getMatchId() {
		return matchId;
	}

	public void setMatchId(Integer matchId) {
		this.matchId = matchId;
	}

}
