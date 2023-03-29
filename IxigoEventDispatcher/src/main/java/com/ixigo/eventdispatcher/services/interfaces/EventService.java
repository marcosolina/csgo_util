package com.ixigo.eventdispatcher.services.interfaces;

import com.ixigo.eventdispatcher.enums.EventType;
import com.ixigo.library.errors.IxigoException;

import reactor.core.publisher.Mono;

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
    public Mono<Void> newIncomingEventFromServer(EventType event, String clientIp);

    /**
     * It register a new event listener
     * 
     * @param listenerUrl
     * @param event
     * @return
     * @throws IxigoException
     */
    public Mono<Boolean> registerNewListener(String listenerUrl, EventType event) throws IxigoException;

    /**
     * It removes the event listener
     * 
     * @param listenerUrl
     * @param event
     * @return
     * @throws IxigoException
     */
    public Mono<Boolean> deleteListener(String listenerUrl, EventType event) throws IxigoException;
}
