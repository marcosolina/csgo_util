package com.marco.csgoutil.roundparser.model.entities;

public class DaoMapPlayed {
    private String mapName;
    private Long count;

    public DaoMapPlayed(String mapName, Long count) {
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
