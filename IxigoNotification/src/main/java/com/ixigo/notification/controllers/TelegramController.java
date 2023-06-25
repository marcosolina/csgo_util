package com.ixigo.notification.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ixigo.library.mediators.web.interfaces.WebMediator;
import com.ixigo.notification.commands.notifications.TelegramCmd;
import com.ixigo.notification.model.rest.telegram.RestTelegramMessage;

import io.swagger.annotations.ApiOperation;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/telegram")
public class TelegramController {

private static final Logger _LOGGER = LoggerFactory.getLogger(TelegramController.class);
	
	@Autowired
	private WebMediator mediator;
	
	@PostMapping
	@ApiOperation(value = "Endpoint used to send Telegram messages")
	public Mono<ResponseEntity<Void>> sendNotification(@RequestBody RestTelegramMessage message) {
		_LOGGER.trace("Inside public class NotificationController.sendNotification");
		return mediator.send(new TelegramCmd(message));
	}
}
