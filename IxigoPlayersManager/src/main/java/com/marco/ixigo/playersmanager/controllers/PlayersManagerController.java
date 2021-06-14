package com.marco.ixigo.playersmanager.controllers;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.marco.ixigo.playersmanager.enums.PartitionType;
import com.marco.ixigo.playersmanager.enums.ScoreType;
import com.marco.ixigo.playersmanager.models.dto.Team;
import com.marco.ixigo.playersmanager.models.rest.Teams;
import com.marco.ixigo.playersmanager.services.interfaces.PlayersManager;
import com.marco.utils.MarcoException;

import io.swagger.annotations.ApiOperation;

@RestController
public class PlayersManagerController {
    private static final Logger _LOGGER = LoggerFactory.getLogger(PlayersManagerController.class);

    @Autowired
    private PlayersManager service;

    @GetMapping("/teams")
    @ApiOperation(value = "It will calculate the \"average\" score of the users provided in the input list. "
            + "The average will be calculated on the last \"counter\" games and then it will \"equally\" "
            + "split the users in different in different teams (teamsCounter) ")
    // @formatter:off
    public ResponseEntity<Teams> getTeams(
            @RequestParam(name = "teamsCounter", defaultValue = "2") Integer teamsCounter,
            @RequestParam(name = "gamesCounter", defaultValue = "50") Integer gamesCounter,
            @RequestParam("usersIDs") List<String> usersIDs,
            @RequestParam(name = "partitionType", defaultValue = "IXIGO") PartitionType partitionType,
            @RequestParam(name = "penaltyWeigth", defaultValue = "0.4") Double penaltyWeigth,
            @RequestParam(name = "partitionScore", defaultValue = "HLTV") ScoreType partitionScore,
            @RequestParam(name = "minPercPlayed", defaultValue = "0.9") BigDecimal minPercPlayed) {
        // @formatter:on
        _LOGGER.trace("Inside PlayersManagerController.getTeams");

        Teams resp = new Teams();
        try {
            List<Team> teams = null;
            switch (partitionType) {
            case IXIGO:
                teams = service.generateTwoTeamsForcingSimilarTeamSizes(gamesCounter, usersIDs, penaltyWeigth,
                        partitionScore, minPercPlayed);
                break;
            case FORCE_PLAYERS:
                teams = service.generateTeamsForcingSimilarTeamSizes(teamsCounter, gamesCounter, usersIDs,
                        penaltyWeigth, partitionScore, minPercPlayed);
                break;
            case SIMPLE:
                teams = service.generateTeams(teamsCounter, gamesCounter, usersIDs, partitionScore, minPercPlayed);
                break;
            default:
                break;
            }
            resp.setTeams(teams);
            resp.setStatus(true);
            return new ResponseEntity<>(resp, HttpStatus.OK);
        } catch (MarcoException e) {
            resp.addError(e);
            return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
        }
    }
}
