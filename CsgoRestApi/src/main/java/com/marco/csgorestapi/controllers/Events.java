package com.marco.csgorestapi.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.marco.csgorestapi.enums.EventType;
import com.marco.csgorestapi.model.rest.EventHttpRequest;
import com.marco.csgorestapi.model.rest.EventListenerHttpRequest;
import com.marco.csgorestapi.model.rest.EventSimpleHttpResponse;
import com.marco.csgorestapi.services.interfaces.EventService;
import com.marco.utils.MarcoException;

import io.swagger.annotations.ApiOperation;

/**
 * This controller send and receives the CSGO event
 * 
 * @author Marco
 *
 */
@RequestMapping("/event")
@RestController
public class Events {
    private static final Logger LOGGER = LoggerFactory.getLogger(Events.class);

    @Autowired
    private EventService service;

    @PostMapping()
    @ApiOperation(value = "It receives the CSGO Server events", code = 200)
    public ResponseEntity<Void> receiveServerEvent(@RequestBody EventHttpRequest request) {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(String.format("New event received: %s", request.toString()));
        }

        if (EventType.fromString(request.getEventName()) != null) {
            new Thread(() -> service.newIncomingEventFromServer(EventType.fromString(request.getEventName()))).start();
            LOGGER.trace("Leaving Events.receiveServerEvent");
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/register")
    @ApiOperation(value = "It receives the CSGO Server events", code = 200)
    public ResponseEntity<EventSimpleHttpResponse> registerListener(@RequestBody EventListenerHttpRequest request) {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(String.format("Registering listener: %s - %s", request.getUrl(), request.getEventType()));
        }
        EventSimpleHttpResponse resp = new EventSimpleHttpResponse();
        try {
            resp.setStatus(service.registerNewListener(request.getUrl(), request.getEventType()));
            return new ResponseEntity<>(resp, HttpStatus.OK);
        } catch (MarcoException e) {
            resp.addError(e);
        }
        return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/unregister")
    @ApiOperation(value = "It receives the CSGO Server events", code = 200)
    public ResponseEntity<EventSimpleHttpResponse> unregisterListener(@RequestBody EventListenerHttpRequest request) {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(String.format("UnRegistering listener: %s - %s", request.getUrl(), request.getEventType()));
        }
        EventSimpleHttpResponse resp = new EventSimpleHttpResponse();
        try {
            resp.setStatus(service.deleteListener(request.getUrl(), request.getEventType()));
            return new ResponseEntity<>(resp, HttpStatus.OK);
        } catch (MarcoException e) {
            resp.addError(e);
        }

        return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
    }

}