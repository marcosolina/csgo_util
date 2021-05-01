package com.marco.csgoutil.roundparser.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.marco.csgoutil.roundparser.model.rest.events.ListenerMessage;
import com.marco.csgoutil.roundparser.utils.RoundParserUtils;

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

    @PostMapping(RoundParserUtils.MAPPING_POST_EVENTS)
    @ApiOperation(value = "Endpoint used to receives the CSGO Events")
    public ResponseEntity<Void> csgoEvent(@RequestBody ListenerMessage request) {
        if (_LOGGER.isInfoEnabled()) {
            _LOGGER.info(String.format("Received CSGO event: %s", request.toString()));
        }

        /*
         * TODO Decide what to do...
         */

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
