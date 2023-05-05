package com.ixigo.demmanager.handlers.events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.ixigo.demmanager.commands.events.EventReceivedCmd;
import com.ixigo.demmanager.services.interfaces.NotificationService;
import com.ixigo.library.mediators.web.interfaces.WebCommandHandler;

import reactor.core.publisher.Mono;

@Component
public class EventHandler implements WebCommandHandler<EventReceivedCmd, Void> {
	private static final Logger _LOGGER = LoggerFactory.getLogger(EventHandler.class);
	@Autowired
	private NotificationService notification;

	@Override
	public Mono<ResponseEntity<Void>> handle(EventReceivedCmd cmd) {
		_LOGGER.trace("Inside EventHandler.handle");

		
		Runnable r = null;
		switch (cmd.getEventReceived().getEventType()) {
		case AZ_START_DEPLOY_VM:
			r = () -> notification.sendParsingCompleteNotification("Azure", "Creating the VM").subscribe(b -> _LOGGER.info("Creating the VM"));
			break;
		case AZ_START_CONFIGURING_VM:
			r = () -> notification.sendParsingCompleteNotification("Azure", "Configuring the VM").subscribe(b -> _LOGGER.info("Configuring the VM"));
			break;
		case AZ_START_INSTALLING_CSGO:
			r = () -> notification.sendParsingCompleteNotification("Azure", "Installing CSGO").subscribe(b -> _LOGGER.info("Installing CSGO"));
			break;
		case AZ_START_CSGO:
			r = () -> notification.sendParsingCompleteNotification("Azure", "Starting CSGO").subscribe(b -> _LOGGER.info("Starting CSGO"));
			break;
		case AZ_START_DELETE_RESOURCE:
			r = () -> notification.sendParsingCompleteNotification("Azure", "Deleting VM").subscribe(b -> _LOGGER.info("Deleting VM"));
			break;
		default:
			break;
		}

		if (r != null) {
			new Thread(r).start();
		}

		return Mono.just(new ResponseEntity<Void>(HttpStatus.OK));
		
	}

}
