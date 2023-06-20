package com.ixigo.notification.services.implementations;

import com.ixigo.notification.services.interfaces.NotificationService;

import reactor.core.publisher.Mono;

public class TelegramNotification implements NotificationService {

	@Override
	public Mono<Boolean> sendNotification(String title, String message) {
		// TODO Auto-generated method stub
		return null;
	}

}
