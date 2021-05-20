package com.marco.ixigoserverhelper.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.marco.ixigoserverhelper.enums.EventType;
import com.marco.ixigoserverhelper.models.rest.GenericHttpResponse;
import com.marco.ixigoserverhelper.models.rest.events.ListenerMessage;
import com.marco.ixigoserverhelper.services.interfaces.DemFilesService;
import com.marco.ixigoserverhelper.services.interfaces.IxiGoEventMonitor;
import com.marco.utils.MarcoException;

@RestController
public class MainController {
    private static final Logger _LOGGER = LoggerFactory.getLogger(MainController.class);

    @Autowired
    private DemFilesService demService;
    @Autowired
    private IxiGoEventMonitor eventMonitorService;

    @PostMapping("/event")
    public ResponseEntity<GenericHttpResponse> ixigoEventListener(@RequestBody ListenerMessage request) {
        if (_LOGGER.isInfoEnabled()) {
            _LOGGER.info(String.format("Received CSGO event: %s", request.toString()));
        }

        if (request.getEventType() == EventType.WARMUP_START || request.getEventType() == EventType.SHUT_DOWN) {
            /*
             * Detach from the main flow
             */
            new Thread(() -> {
                try {
                    demService.scpLastDemFiles();
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
    
    @GetMapping("/event")
    public ResponseEntity<String> ixigoEventListener() {
        return new ResponseEntity<>(eventMonitorService.checkForNewEvent(), HttpStatus.OK);
    }
}
