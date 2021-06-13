package com.marco.ixigo.demmanager.model.entities;

public class DaoMapGamesCounter {
    private String mapName;
    private Long count;

    public DaoMapGamesCounter(String mapName, Long count) {
        super();
        this.mapName = mapName;
        this.count = count;
    }

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
