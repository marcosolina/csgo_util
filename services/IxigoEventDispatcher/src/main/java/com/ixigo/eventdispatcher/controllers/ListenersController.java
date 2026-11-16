package com.ixigo.eventdispatcher.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ixigo.eventdispatcher.commands.listeners.DeleteListenerCmd;
import com.ixigo.eventdispatcher.commands.listeners.PostListenerCmd;
import com.ixigo.eventdispatcher.models.rest.EventListenerResgistraionRequest;
import com.ixigo.library.mediators.web.interfaces.WebMediator;

import io.swagger.annotations.ApiOperation;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/listener")
public class ListenersController {
	private static final Logger LOGGER = LoggerFactory.getLogger(ListenersController.class);

	@Autowired
	private WebMediator mediator;
	
	@PostMapping
	@ApiOperation(value = "End point to use if you want to register your event listener", code = 201)
    public Mono<ResponseEntity<Void>> registerListener(@RequestBody EventListenerResgistraionRequest request) {
		LOGGER.trace("Inside ListenersController.registerListener");
		return mediator.send(new PostListenerCmd(request.getEventType(), request.getUrl()));
    }
	
	@DeleteMapping
	@ApiOperation(value = "End point to use if you want to register your event listener", code = 201)
    public Mono<ResponseEntity<Void>> unregisterListener(@RequestBody EventListenerResgistraionRequest request) {
		LOGGER.trace("Inside ListenersController.unregisterListener");
		return mediator.send(new DeleteListenerCmd(request.getEventType(), request.getUrl()));
    }
}
