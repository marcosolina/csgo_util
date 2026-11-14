package com.ixigo.eventdispatcher.handlers.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.ixigo.eventdispatcher.commands.listeners.PostListenerCmd;
import com.ixigo.eventdispatcher.services.interfaces.EventService;
import com.ixigo.library.mediators.web.interfaces.WebCommandHandler;

import reactor.core.publisher.Mono;

@Component
public class PostListenerCmdHandler implements WebCommandHandler<PostListenerCmd, Void>{
	private static final Logger _LOGGER = LoggerFactory.getLogger(PostListenerCmdHandler.class);
	@Autowired
	private EventService service;
	
	@Override
	public Mono<ResponseEntity<Void>> handle(PostListenerCmd request) {
		_LOGGER.trace("Inside PostListenerCmdHandler.handle");
		
		return service.registerNewListener(request.getListenerUrl(), request.getListenForEventType())
			.map(b -> b ? new ResponseEntity<Void>(HttpStatus.CREATED) : new ResponseEntity<Void>(HttpStatus.BAD_REQUEST))
			.defaultIfEmpty(new ResponseEntity<Void>(HttpStatus.BAD_REQUEST));
	}

}
