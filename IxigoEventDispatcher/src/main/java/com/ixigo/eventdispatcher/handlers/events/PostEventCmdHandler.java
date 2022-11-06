package com.ixigo.eventdispatcher.handlers.events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.ixigo.eventdispatcher.commands.events.PostEventCmd;
import com.ixigo.eventdispatcher.enums.EventType;
import com.ixigo.eventdispatcher.services.interfaces.EventService;
import com.ixigo.library.errors.IxigoException;
import com.ixigo.library.mediators.web.interfaces.WebCommandHandler;
import com.ixigo.library.messages.IxigoMessageResource;

import reactor.core.publisher.Mono;

@Component
public class PostEventCmdHandler implements WebCommandHandler<PostEventCmd, Void> {
	private static final Logger _LOGGER = LoggerFactory.getLogger(PostEventCmdHandler.class);
	@Autowired
	private EventService service;
	@Autowired
	private IxigoMessageResource msgSource;

	@Override
	public Mono<ResponseEntity<Void>> handle(PostEventCmd request) {
		_LOGGER.trace("Inside PostEventCmdHandler.handle");
		EventType et = EventType.fromString(request.getCsgoServerEventType());
		if (et == null) {
			return Mono.error(new IxigoException(HttpStatus.BAD_REQUEST, msgSource.getMessage("DISP00004")));
		}

		new Thread(() -> {
			service.newIncomingEventFromServer(et, request.getCsgoServerIpAddress()).subscribe(v -> {
				_LOGGER.debug(String.format("Event: %s from server: %s dispatched", et.name(), request.getCsgoServerIpAddress()));
			});
		}).start();

		return Mono.just(new ResponseEntity<>(HttpStatus.ACCEPTED));
	}

}
