package com.marco.csgoutil.roundparser.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.marco.csgoutil.roundparser.model.rest.rcon.ActivePlayersResponse;
import com.marco.csgoutil.roundparser.model.rest.rcon.MovePlayersResponse;
import com.marco.csgoutil.roundparser.services.interfaces.RconService;
import com.marco.csgoutil.roundparser.utils.RoundParserUtils;
import com.marco.utils.MarcoException;

import io.swagger.annotations.ApiOperation;

/**
 * This controller provides APIs that will interact with the CSGO RCON console
 * 
 * @author Marco
 *
 */
@Controller
public class RconController {
    private static final Logger _LOGGER = LoggerFactory.getLogger(RconController.class);

    @Autowired
    private RconService rconService;

    @GetMapping(RoundParserUtils.MAPPING_GET_MOVE_PLAYERS)
    @ApiOperation(value = "It will move the input players into the Terrorist team and al the remaining into the CT team")
    public ResponseEntity<MovePlayersResponse> movePlayers(@RequestParam("terroristIDs") List<String> terroristIDs) {
        _LOGGER.trace("Inside RconController.movePlayers");

        MovePlayersResponse resp = new MovePlayersResponse();
        try {
            resp.setStatus(rconService.moveSpecifiedPlayerToTteam(terroristIDs));
        } catch (MarcoException e) {
            resp.addError(e);
        }

        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

    @GetMapping(RoundParserUtils.MAPPING_GET_ACTIVE_PLAYERS)
    @ApiOperation(value = "It returns the steam IDs of the current active players")
    public ResponseEntity<ActivePlayersResponse> activePlayers() {
        _LOGGER.trace("Inside RconController.movePlayers");

        ActivePlayersResponse resp = new ActivePlayersResponse();
        try {
            resp.setPlayers(rconService.getCurrentPlayersIds());
        } catch (MarcoException e) {
            resp.addError(e);
        }

        return new ResponseEntity<>(resp, HttpStatus.OK);
    }
}
