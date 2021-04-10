package com.marco.csgoutil.roundparser.services.implementations.notifications;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;

import com.marco.csgoutil.roundparser.services.interfaces.NotificationService;

public class TelegramNotificationService implements NotificationService {
	private static final Logger LOGGER = LoggerFactory.getLogger(TelegramNotificationService.class);
	
	@Value("${com.marco.csgoutil.notification.telegram.enabled}")
	private boolean notificationEnabled;
	@Value("${com.marco.csgoutil.notification.telegram.telegramToken}")
	private String botToken;
	@Value("${com.marco.csgoutil.notification.telegram.chatGroupId}")
	private String chatId;
	
	@Autowired
	private WebClient.Builder wcb;
	
	@Override
	public void sendParsingCompleteNotification(String title, String message) {
		if(!notificationEnabled) {
			return;
		}
		
		LOGGER.info("Sending Telegram notification");
		WebClient wb = wcb.baseUrl(String.format("https://api.telegram.org/bot%s/sendMessage", botToken)).build();
		// @formatter:off
		wb.get()
			.uri(uriFunction -> 
				uriFunction
					.queryParam("chat_id", chatId)
					.queryParam("text", String.format("%s%n%n%s", title, message))
					.build()
			)
			.retrieve()
			.bodyToMono(String.class).block();
		// @formatter:on
		LOGGER.info("Telegram notification sent");
	}

}
