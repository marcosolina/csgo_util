package com.ixigo.eventdispatcher.services.interfaces;

import reactor.core.publisher.Mono;

/**
 * This interface provides some methods to send notifications
 * 
 * @author Marco
 *
 */
public interface NotificationService {

    /**
     * It will notify that an error occur while dispatching an event
     * 
     * @param recipients
     */
    public Mono<Boolean> sendEventServiceError(String title, String message);

}
