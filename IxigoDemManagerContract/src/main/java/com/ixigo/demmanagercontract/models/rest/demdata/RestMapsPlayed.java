package com.ixigo.demmanagercontract.models.rest.demdata;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Response model returned when retrieving the list of maps that we played
 * 
 * @author Marco
 *
 */
public class RestMapsPlayed  implements Serializable {
	private static final long serialVersionUID = 1L;
	@JsonProperty("maps")
	private List<RestMapPlayed> maps;

    public List<RestMapPlayed> getMaps() {
        return maps;
    }

    public void setMaps(List<RestMapPlayed> maps) {
        this.maps = maps;
    }
}
