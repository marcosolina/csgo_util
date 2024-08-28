package com.ixigo.serverhelper.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.ixigo.eventdispatcher.enums.EventType;
import com.ixigo.library.mediators.web.interfaces.WebCommandHandler;
import com.ixigo.serverhelper.commands.EventReceivedCmd;
import com.ixigo.serverhelper.services.interfaces.DemFilesService;

import reactor.core.publisher.Mono;

@Component
public class EventReceivedCmdHandler implements WebCommandHandler<EventReceivedCmd, Void> {
	private static final Logger LOGGER = LoggerFactory.getLogger(EventReceivedCmdHandler.class);

	@Autowired
	private DemFilesService service;

	@Override
	public Mono<ResponseEntity<Void>> handle(EventReceivedCmd cmd) {
		LOGGER.trace("Inside EventReceivedCmdHandler.handle");
		switch (cmd.getEventReceived().getEventType()) {
		case WARMUP_START:
		case SHUT_DOWN:
		case WARMUP_END:
			new Thread(() -> {
				service.postLastDemFiles(cmd.getEventReceived().getEventType() == EventType.SHUT_DOWN)
				.subscribe(b -> LOGGER.debug("Post dem file resul: " + b));
			}).start();
			break;
		default:
			break;
		}
		return Mono.just(new ResponseEntity<>(HttpStatus.OK));
	}

}
