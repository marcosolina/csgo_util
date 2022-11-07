package com.ixigo.playersmanager.models.svc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SvcMapStats {
	private String mapName;
    private LocalDateTime playedOn;
    private List<SvcUserMapStats> usersStats;

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

    public List<SvcUserMapStats> getUsersStats() {
        return usersStats;
    }

    public void setUsersStats(List<SvcUserMapStats> usersStats) {
        this.usersStats = usersStats;
    }

    public boolean addUserMapStats(SvcUserMapStats ums) {
        if (usersStats == null) {
            usersStats = new ArrayList<>();
        }
        return usersStats.add(ums);
    }
}
