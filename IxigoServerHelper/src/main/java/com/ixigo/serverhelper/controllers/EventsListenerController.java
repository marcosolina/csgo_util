package com.ixigo.serverhelper.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ixigo.eventdispatcher.models.rest.DispatchedEventMessage;
import com.ixigo.library.mediators.web.interfaces.WebMediator;
import com.ixigo.serverhelper.commands.EventReceivedCmd;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/event")
public class EventsListenerController {
	private static final Logger LOGGER = LoggerFactory.getLogger(EventsListenerController.class);
	@Autowired
	private WebMediator mediator;

	@PostMapping
	public Mono<ResponseEntity<Void>> ixigoEventListener(@RequestBody DispatchedEventMessage event) {
		LOGGER.trace("Inside EventsListenerController.ixigoEventListener");
		return mediator.send(new EventReceivedCmd(event));
	}
}
