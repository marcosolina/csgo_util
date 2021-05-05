package com.marco.discordbot.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.marco.discordbot.model.rest.GenericResponse;
import com.marco.discordbot.model.rest.GetMembersResponse;
import com.marco.discordbot.services.interfaces.IxiGoBot;
import com.marco.utils.MarcoException;

@RestController
public class MainController {
    private static final Logger LOGGER = LoggerFactory.getLogger(MainController.class);

    @Autowired
    private IxiGoBot ixiGoBot;

    @PostMapping("/start")
    public ResponseEntity<GenericResponse> startBot() {
        GenericResponse resp = new GenericResponse();
        try {
            LOGGER.debug("Starting the bot");
            ixiGoBot.start();
            resp.setStatus(true);
        } catch (MarcoException e) {
            if(LOGGER.isTraceEnabled()) {
                e.printStackTrace();
            }
            resp.addError(e);
        }
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

    @DeleteMapping("/stop")
    public ResponseEntity<GenericResponse> endBot() {
        GenericResponse resp = new GenericResponse();
        try {
            LOGGER.debug("Stopping the bot");
            ixiGoBot.stop();
            resp.setStatus(true);
        } catch (MarcoException e) {
            if(LOGGER.isTraceEnabled()) {
                e.printStackTrace();
            }
            resp.addError(e);
        }
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

    @GetMapping("/members")
    public ResponseEntity<GetMembersResponse> get() {
        GetMembersResponse resp = new GetMembersResponse();
        try {
            LOGGER.debug("Getting the members of the server");
            resp.setMembers(ixiGoBot.getMembers());
        } catch (MarcoException e) {
            if(LOGGER.isTraceEnabled()) {
                e.printStackTrace();
            }
            resp.addError(e);
        }
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }
}
