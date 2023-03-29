package com.ixigo.demmanager.services.interfaces;

import reactor.core.publisher.Mono;

public interface NotificationService {
	/**
     * It will notify the recipients that the parsing process is complete
     * 
     * @param recipients
     */
    public Mono<Boolean> sendParsingCompleteNotification(String title, String message);
}
