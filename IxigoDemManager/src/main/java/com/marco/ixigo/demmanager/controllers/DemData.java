package com.marco.ixigo.demmanager.controllers;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.marco.ixigo.demmanager.model.rest.demdata.MapsPlayed;
import com.marco.ixigo.demmanager.model.rest.demdata.ScoreTypes;
import com.marco.ixigo.demmanager.model.rest.demdata.Users;
import com.marco.ixigo.demmanager.model.rest.demdata.UsersScores;
import com.marco.ixigo.demmanager.services.interfaces.DemFileParser;
import com.marco.utils.MarcoException;

import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping(value = "/demdata")
public class DemData {
    private static final Logger _LOGGER = LoggerFactory.getLogger(DemData.class);
    
    @Autowired
    private DemFileParser service;
    
    
    @GetMapping("/scorestype")
    @ApiOperation(value = "It will return a map of the score types that you can pass when call the team creation API")
    public ResponseEntity<ScoreTypes> getScoreType() {
        _LOGGER.trace("Inside DemData.getScoreType");

        ScoreTypes resp = new ScoreTypes();
        resp.setTypes(service.mapOfAvailableScores());
        resp.setStatus(true);
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }
    
    @GetMapping("/mapsplayed")
    @ApiOperation(value = "It will returns how many times we played a map")
    public ResponseEntity<MapsPlayed> getMapPlayedCount() {
        _LOGGER.trace("Inside DemData.getMapPlayedCount");

        MapsPlayed resp = new MapsPlayed();
        resp.setMaps(service.countGamesOnAMap());
        resp.setStatus(true);
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }
    
    @GetMapping("/users")
    @ApiOperation(value = "It returns a list of the known users")
    public ResponseEntity<Users> getUsers() {
        _LOGGER.trace("Inside DemData.getUsers");

        Users resp = new Users();
        try {

            resp.setUsers(service.getListOfUsers());
            resp.setStatus(true);
            return new ResponseEntity<>(resp, HttpStatus.OK);
        } catch (MarcoException e) {
            resp.addError(e);
            return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
        }
    }
    
    /**
     * It will return the last "counter" number of scores associated to the Users
     * provided in the input list
     * 
     * @param counter
     * @param usersIDs
     * @return
     */
    @GetMapping("/usersscores")
    @ApiOperation(value = "It will return the last \"counter\" number of scores associated to the Users provided in the input list")
    public ResponseEntity<UsersScores> getUsersLastScores(
            @RequestParam(name = "counter", defaultValue = "50") Integer counter,
            @RequestParam("usersIDs") List<String> usersIDs,
            @RequestParam(name = "minPercPlayed", defaultValue = "0") BigDecimal minPercPlayed) {
        _LOGGER.trace("Inside MainController.getUsersLastScores");

        UsersScores resp = new UsersScores();
        try {
            resp.setUsersScores(service.getUsersStatsForLastXGames(counter, usersIDs, minPercPlayed));
            resp.setStatus(true);
            return new ResponseEntity<>(resp, HttpStatus.OK);
        } catch (MarcoException e) {
            resp.addError(e);
            return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
        }
    }
}