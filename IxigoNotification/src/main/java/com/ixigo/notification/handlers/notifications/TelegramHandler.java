package com.ixigo.notification.handlers.notifications;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.ixigo.library.mediators.web.interfaces.WebCommandHandler;
import com.ixigo.notification.commands.notifications.TelegramCmd;
import com.ixigo.notification.services.interfaces.NotificationService;

import reactor.core.publisher.Mono;

@Component
public class TelegramHandler implements WebCommandHandler<TelegramCmd, Void> {
	private static final Logger _LOGGER = LoggerFactory.getLogger(TelegramHandler.class);

	@Autowired
	private NotificationService service;

	@Override
	public Mono<ResponseEntity<Void>> handle(TelegramCmd cmd) {
		_LOGGER.trace("Inside TelegramHandler.handle");
		// @formatter:off
		return service.sendNotification(cmd.getMessageToSend().getTitle(), cmd.getMessageToSend().getMessage())
				.map(status -> new ResponseEntity<>(status ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR));
		// @formatter:on
	}

}
