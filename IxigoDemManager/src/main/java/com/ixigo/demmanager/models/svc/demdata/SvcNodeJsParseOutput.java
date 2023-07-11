package com.ixigo.demmanager.models.svc.demdata;

import java.io.Serializable;
import java.util.List;

public class SvcNodeJsParseOutput implements Serializable {
	private static final long serialVersionUID = 1L;
	private List<SvcUserGotvScore> allPlayerStats;
	private SvcMapFileStats mapStats;
	private List<SvcRoundStats> allRoundStats;
    private List<SvcWeaponStats> weaponStats;
    private List<SvcKillEvent> killEvents;

	public List<SvcUserGotvScore> getAllPlayerStats() {
		return allPlayerStats;
	}

	public void setAllPlayerStats(List<SvcUserGotvScore> allPlayerStats) {
		this.allPlayerStats = allPlayerStats;
	}

	public SvcMapFileStats getMapStats() {
		return mapStats;
	}

	public void setMapStats(SvcMapFileStats mapStats) {
		this.mapStats = mapStats;
	}

	public List<SvcWeaponStats> getWeaponStats() {
		return weaponStats;
	}

	public void setWeaponStats(List<SvcWeaponStats> weaponStats) {
		this.weaponStats = weaponStats;
	}

	public List<SvcKillEvent> getKillEvents() {
		return killEvents;
	}

	public void setKillEvents(List<SvcKillEvent> killEvents) {
		this.killEvents = killEvents;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public List<SvcRoundStats> getAllRoundStats() {
		return allRoundStats;
	}

	public void setAllRoundStats(List<SvcRoundStats> allRoundStats) {
		this.allRoundStats = allRoundStats;
	}
}
