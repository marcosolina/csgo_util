package com.marco.discordbot.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.marco.discordbot.misc.DiscordBotUtils;
import com.marco.discordbot.model.rest.events.ListenerMessage;
import com.marco.discordbot.services.interfaces.IxiGoBot;
import com.marco.utils.MarcoException;

import io.swagger.annotations.ApiOperation;

/**
 * It receives the CSGO events
 * 
 * @author Marco
 *
 */
@Controller
public class EventsController {
    private static final Logger _LOGGER = LoggerFactory.getLogger(EventsController.class);

    @Autowired
    private IxiGoBot service;

    @PostMapping(DiscordBotUtils.MAPPING_POST_EVENTS)
    @ApiOperation(value = "Endpoint used to receives the CSGO Events")
    public ResponseEntity<Void> csgoEvent(@RequestBody ListenerMessage request) {
        if (_LOGGER.isInfoEnabled()) {
            _LOGGER.info(String.format("Received CSGO event: %s", request.toString()));
        }

        // @formatter:off
        Runnable r = null;
        if(service.isAutobalance()) {
            switch (request.getEventType()) {
            case CS_WIN_PANEL_MATCH:
                r = () -> {try {service.moveAllMembersIntoGeneralChannel();}catch(MarcoException e) {e.printStackTrace();}};
                break;
            case WARMUP_START:
                r = () -> {try {service.warmUpBalanceTeamApi();}catch(MarcoException e) {e.printStackTrace();}};
                break;
            case WARMUP_END:
                r = () -> {
                        try {
                            service.balanceTheTeams();
                            Thread.sleep(1000);
                            service.moveDiscordUsersInTheAppropirateChannel();
                            Thread.sleep(1000);
                            service.restartIxiGoRound();
                        }
                        catch(MarcoException | InterruptedException e) {
                            e.printStackTrace();
                        }
                    };
                break;
            default:
                break;
            }
        }
        
        if(r != null) {
            new Thread(r).start();
        }
        
        // @formatter:on
        if (_LOGGER.isInfoEnabled()) {
            _LOGGER.info(String.format("Sending OK to CSGO event: %s", request.toString()));
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
