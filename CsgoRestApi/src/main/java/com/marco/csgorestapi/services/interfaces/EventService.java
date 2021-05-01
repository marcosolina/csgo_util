package com.marco.csgorestapi.services.interfaces;

import com.marco.csgorestapi.enums.EventType;

/**
 * List of operation that you can do with events
 * 
 * @author Marco
 *
 */
public interface EventService {
    /**
     * It manages a new event sent from the server
     * 
     * @param event
     */
    public void newIncomingEventFromServer(EventType event);
}
