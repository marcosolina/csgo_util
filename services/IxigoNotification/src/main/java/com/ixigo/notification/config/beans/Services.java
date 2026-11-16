package com.ixigo.notification.config.beans;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ixigo.notification.services.implementations.TelegramNotification;
import com.ixigo.notification.services.interfaces.NotificationService;

@Configuration
public class Services {
	@Bean
	public NotificationService getNotificationService() {
		return new TelegramNotification();
	}
}
