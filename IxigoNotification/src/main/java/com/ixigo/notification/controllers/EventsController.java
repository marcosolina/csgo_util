package com.ixigo.notification.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ixigo.discordbot.commands.events.EventReceivedCmd;
import com.ixigo.eventdispatcher.models.rest.DispatchedEventMessage;
import com.ixigo.library.mediators.web.interfaces.WebMediator;

import io.swagger.annotations.ApiOperation;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/event")
public class EventsController {
	private static final Logger _LOGGER = LoggerFactory.getLogger(EventsController.class);
	
	@Autowired
	private WebMediator mediator;
	
	@PostMapping
	@ApiOperation(value = "Endpoint used to receives the CSGO Events")
	public Mono<ResponseEntity<Void>> ixigoEventListener(@RequestBody DispatchedEventMessage event) {
		_LOGGER.trace("Inside EventsListenerController.ixigoEventListener");
		return mediator.send(new EventReceivedCmd(event));
	}
}
