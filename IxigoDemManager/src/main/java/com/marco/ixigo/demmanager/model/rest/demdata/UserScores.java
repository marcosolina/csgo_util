package com.marco.ixigo.demmanager.model.rest.demdata;

import java.util.List;

import com.marco.ixigo.demmanager.model.dto.MapStats;
import com.marco.utils.http.MarcoResponse;

import io.swagger.annotations.ApiModelProperty;

/**
 * REST Model used to return the User scores per game/map played
 * 
 * @author Marco
 *
 */
public class UserScores extends MarcoResponse {
    @ApiModelProperty(notes = "List of single user scores per map/game")
    private List<MapStats> mapsScores;

    public List<MapStats> getMapsScores() {
        return mapsScores;
    }

    public void setMapsScores(List<MapStats> mapsScores) {
        this.mapsScores = mapsScores;
    }

}
