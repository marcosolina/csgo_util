package com.ixigo.eventdispatcher.config.beans;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ixigo.eventdispatcher.services.implementations.EventServiceImpl;
import com.ixigo.eventdispatcher.services.implementations.TelegramNotificationService;
import com.ixigo.eventdispatcher.services.interfaces.EventService;
import com.ixigo.eventdispatcher.services.interfaces.NotificationService;

@Configuration
public class Services {
	@Bean
	public NotificationService getNotificationService() {
		return new TelegramNotificationService();
	}
	
	@Bean
	public EventService getEventService() {
		return new EventServiceImpl();
	}
}
