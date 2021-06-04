package com.marco.discordbot.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.marco.discordbot.enums.BotConfigKey;
import com.marco.discordbot.misc.DiscordBotUtils;
import com.marco.discordbot.model.rest.BotConfig;
import com.marco.discordbot.model.rest.GenericResponse;
import com.marco.discordbot.model.rest.GetConfigValue;
import com.marco.discordbot.model.rest.GetMembersResponse;
import com.marco.discordbot.model.rest.GetPlayersResponse;
import com.marco.discordbot.model.rest.SavePlayersMapping;
import com.marco.discordbot.services.interfaces.IxiGoBot;
import com.marco.utils.MarcoException;

@RestController
public class MainController {
    private static final Logger LOGGER = LoggerFactory.getLogger(MainController.class);

    @Autowired
    private IxiGoBot ixiGoBot;

    @PostMapping(DiscordBotUtils.MAPPING_START_BOT)
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

    @DeleteMapping(DiscordBotUtils.MAPPING_STOP_BOT)
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
    
    @GetMapping(DiscordBotUtils.MAPPING_GET_CONFIG)
    public ResponseEntity<GetConfigValue> getConfig(@RequestParam("config") BotConfigKey key) {
        GetConfigValue resp = new GetConfigValue();
        try {
            LOGGER.debug("Get config value");
            resp.setConfig(ixiGoBot.getBotConfig(key));
            resp.setStatus(true);
        } catch (MarcoException e) {
            if(LOGGER.isTraceEnabled()) {
                e.printStackTrace();
            }
            resp.addError(e);
        }
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }
    
    @PutMapping(DiscordBotUtils.MAPPING_PUT_CONFIG)
    public ResponseEntity<GetPlayersResponse> updateBotConfig(@RequestBody BotConfig config) {
        GetPlayersResponse resp = new GetPlayersResponse();
        try {
            LOGGER.debug("Update config value");
            resp.setStatus(ixiGoBot.updateBotConfig(config));
        } catch (MarcoException e) {
            if(LOGGER.isTraceEnabled()) {
                e.printStackTrace();
            }
            resp.addError(e);
        }
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }
    
    @GetMapping(DiscordBotUtils.MAPPING_GET_MAPPED_PLAYERS)
    public ResponseEntity<GetPlayersResponse> getPlayers() {
        GetPlayersResponse resp = new GetPlayersResponse();
        try {
            LOGGER.debug("Getting the list of known players");
            resp.setPlayers(ixiGoBot.getListOfMappedPlayers());
            resp.setStatus(true);
        } catch (MarcoException e) {
            if(LOGGER.isTraceEnabled()) {
                e.printStackTrace();
            }
            resp.addError(e);
        }
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }
    
    @PostMapping(DiscordBotUtils.MAPPING_STORE_MAPPED_PLAYERS)
    public ResponseEntity<GenericResponse> storePlayerDetails(@RequestBody SavePlayersMapping players) {
        GenericResponse resp = new GenericResponse();
        try {
            LOGGER.debug("Storing player details");
            resp.setStatus(ixiGoBot.storePlayersDetails(players.getPlayers()));
        } catch (MarcoException e) {
            if(LOGGER.isTraceEnabled()) {
                e.printStackTrace();
            }
            resp.addError(e);
        }
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

    @GetMapping(DiscordBotUtils.MAPPING_GET_DISCORD_USERS)
    public ResponseEntity<GetMembersResponse> get() {
        GetMembersResponse resp = new GetMembersResponse();
        try {
            LOGGER.debug("Getting the members of the server");
            resp.setMembers(ixiGoBot.getAllDiscordServerMembers());
        } catch (MarcoException e) {
            if(LOGGER.isTraceEnabled()) {
                e.printStackTrace();
            }
            resp.addError(e);
        }
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }
    
    @PutMapping(DiscordBotUtils.MAPPING_POST_AUTO_BALANCE)
    public ResponseEntity<GenericResponse> setAutoBalance() {
        GenericResponse resp = new GenericResponse();
        try {
            ixiGoBot.setAutoBalance(!ixiGoBot.isAutobalance());
            LOGGER.debug("Autobalance set to: " + ixiGoBot.isAutobalance());
            resp.setStatus(true);
        } catch (Exception e) {
            if(LOGGER.isTraceEnabled()) {
                e.printStackTrace();
            }
            resp.addError(new MarcoException(e));
        }
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }
}
