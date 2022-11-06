package com.ixigo.eventdispatcher.handlers.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.ixigo.eventdispatcher.commands.listeners.DeleteListenerCmd;
import com.ixigo.eventdispatcher.services.interfaces.EventService;
import com.ixigo.library.mediators.web.interfaces.WebCommandHandler;

import reactor.core.publisher.Mono;

@Component
public class DeleteListenerCmdHandler implements WebCommandHandler<DeleteListenerCmd, Void>{
	private static final Logger _LOGGER = LoggerFactory.getLogger(DeleteListenerCmdHandler.class);
	@Autowired
	private EventService service;
	
	@Override
	public Mono<ResponseEntity<Void>> handle(DeleteListenerCmd cmd) {
		_LOGGER.trace("Inside DeleteListenerCmdHandler.handle");
		
		return service.deleteListener(cmd.getListenerUrl(), cmd.getListenForEventType())
			.map(b -> b ? new ResponseEntity<Void>(HttpStatus.CREATED) : new ResponseEntity<Void>(HttpStatus.BAD_REQUEST))
			.defaultIfEmpty(new ResponseEntity<Void>(HttpStatus.BAD_REQUEST));
	}

}
