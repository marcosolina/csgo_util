package com.marco.ixigo.serverhelper.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.marco.ixigo.serverhelper.enums.EventType;
import com.marco.ixigo.serverhelper.model.events.ListenerMessage;
import com.marco.ixigo.serverhelper.model.rest.GenericHttpResponse;
import com.marco.ixigo.serverhelper.services.interfaces.DemFilesService;
import com.marco.utils.MarcoException;

/**
 * Simple Spring Controller
 * 
 * @author Marco
 *
 */
@RestController
public class MainController {
    private static final Logger _LOGGER = LoggerFactory.getLogger(MainController.class);

    @Autowired
    private DemFilesService demService;

    @PostMapping("/event")
    public ResponseEntity<GenericHttpResponse> ixigoEventListener(@RequestBody ListenerMessage request) {
        if (_LOGGER.isDebugEnabled()) {
            _LOGGER.debug(String.format("Received CSGO event: %s", request.toString()));
        }

        if (request.getEventType() == EventType.WARMUP_START || request.getEventType() == EventType.SHUT_DOWN) {
            /*
             * Detach from the main flow
             */
            new Thread(() -> {
                try {
                    demService.sendLastDemFiles();
                } catch (MarcoException e) {
                    _LOGGER.error(e.getMessage());
                    e.printStackTrace();
                }
            }).start();
        }

        GenericHttpResponse resp = new GenericHttpResponse();
        resp.setStatus(true);
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }
}
