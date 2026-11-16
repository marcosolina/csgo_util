package com.ixigo.notification.services.interfaces;

import reactor.core.publisher.Mono;

public interface NotificationService {
	/**
     * It will send a notification
     * 
     * @param recipients
     */
    public Mono<Boolean> sendNotification(String title, String message);
}
