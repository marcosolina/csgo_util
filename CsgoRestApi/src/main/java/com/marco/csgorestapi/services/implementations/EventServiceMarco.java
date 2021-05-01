package com.marco.csgorestapi.services.implementations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.marco.csgorestapi.enums.EventType;
import com.marco.csgorestapi.services.interfaces.EventService;

public class EventServiceMarco implements EventService {
    private static final Logger LOGGER = LoggerFactory.getLogger(EventServiceMarco.class);

    @Override
    public void newIncomingEventFromServer(EventType event) {
        LOGGER.debug(String.format("Processing event: %s", event.name()));
    }

}
