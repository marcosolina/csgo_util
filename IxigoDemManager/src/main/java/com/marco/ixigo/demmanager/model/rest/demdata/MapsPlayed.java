package com.marco.ixigo.demmanager.model.rest.demdata;

import java.util.List;

import com.marco.utils.http.MarcoResponse;

/**
 * Response model returned when retrieving the list of maps that we played
 * 
 * @author Marco
 *
 */
public class MapsPlayed extends MarcoResponse {
    private List<MapPlayed> maps;

    public List<MapPlayed> getMaps() {
        return maps;
    }

    public void setMaps(List<MapPlayed> maps) {
        this.maps = maps;
    }

}
