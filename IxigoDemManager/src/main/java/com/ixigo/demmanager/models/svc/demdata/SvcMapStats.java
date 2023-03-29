package com.ixigo.demmanager.models.svc.demdata;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SvcMapStats {
	private String mapName;
    private LocalDateTime playedOn;
    private List<SvcUserGotvScore> usersStats;

    public String getMapName() {
        return mapName;
    }

    public void setMapName(String mapName) {
        this.mapName = mapName;
    }

    public LocalDateTime getPlayedOn() {
        return playedOn;
    }

    public void setPlayedOn(LocalDateTime playedOn) {
        this.playedOn = playedOn;
    }

    public List<SvcUserGotvScore> getUsersStats() {
        return usersStats;
    }

    public void setUsersStats(List<SvcUserGotvScore> usersStats) {
        this.usersStats = usersStats;
    }

    public boolean addUserMapStats(SvcUserGotvScore ums) {
        if (usersStats == null) {
            usersStats = new ArrayList<>();
        }
        return usersStats.add(ums);
    }
}
