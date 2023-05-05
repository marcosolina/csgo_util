package com.ixigo.demmanager.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ixigo.demmanager.commands.events.EventReceivedCmd;
import com.ixigo.eventdispatcher.models.rest.DispatchedEventMessage;
import com.ixigo.library.mediators.web.interfaces.WebMediator;

import io.swagger.annotations.ApiOperation;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/events")
public class EventsListenerController {
	private static final Logger _LOGGER = LoggerFactory.getLogger(EventsListenerController.class);
	
	@Autowired
	private WebMediator mediator;

	@PostMapping
	@ApiOperation(value = "Endpoint used to receives the CSGO Events")
	public Mono<ResponseEntity<Void>> ixigoEventListener(@RequestBody DispatchedEventMessage event) {
		_LOGGER.trace("Inside EventsListenerController.ixigoEventListener");
		return mediator.send(new EventReceivedCmd(event));
	}
}
