package com.ixigo.demmanager.models.rest.demdata;

import java.util.List;

/**
 * Response model returned when retrieving the list of maps that we played
 * 
 * @author Marco
 *
 */
public class RestMapsPlayed {
	private List<RestMapPlayed> maps;

    public List<RestMapPlayed> getMaps() {
        return maps;
    }

    public void setMaps(List<RestMapPlayed> maps) {
        this.maps = maps;
    }
}
