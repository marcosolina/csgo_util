package com.marco.csgorestapi.services.interfaces;

import com.marco.csgorestapi.enums.EventType;
import com.marco.utils.MarcoException;

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

    /**
     * It register a new event listener
     * 
     * @param listenerUrl
     * @param event
     * @return
     * @throws MarcoException
     */
    public boolean registerNewListener(String listenerUrl, EventType event) throws MarcoException;

    /**
     * It removes the event listener
     * 
     * @param listenerUrl
     * @param event
     * @return
     * @throws MarcoException
     */
    public boolean deleteListener(String listenerUrl, EventType event) throws MarcoException;
}