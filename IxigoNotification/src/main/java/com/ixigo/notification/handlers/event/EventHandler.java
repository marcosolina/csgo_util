package com.ixigo.notification.handlers.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.ixigo.library.mediators.web.interfaces.WebCommandHandler;
import com.ixigo.notification.commands.events.EventReceivedCmd;
import com.ixigo.notification.services.interfaces.NotificationService;

import reactor.core.publisher.Mono;

@Component
public class EventHandler implements WebCommandHandler<EventReceivedCmd, Void> {
	private static final Logger _LOGGER = LoggerFactory.getLogger(EventHandler.class);
	private static final String TITLE = "Event received";

	@Autowired
	private NotificationService service;

	@Override
	public Mono<ResponseEntity<Void>> handle(EventReceivedCmd cmd) {
		_LOGGER.trace("Inside EventHandler.handle");

		_LOGGER.info(String.format("Received event: %s", cmd.getEventReceived().getEventType().getDesc()));

		Mono<Boolean> mono = null;
		switch (cmd.getEventReceived().getEventType()) {
		case AZ_START_DEPLOY_VM:
			mono = service.sendNotification(TITLE, "Starting to create the Linux VM");
			break;
		case AZ_START_CONFIGURING_VM:
			mono = service.sendNotification(TITLE, "Installing software on the Linux VM");
			break;
		case AZ_START_INSTALLING_CSGO:
			mono = service.sendNotification(TITLE, "Updating CSGO");
			break;
		case AZ_START_DELETE_RESOURCE:
			mono = service.sendNotification(TITLE, "Deleting the VM");
			break;
		default:
			break;
		}

		Mono<Boolean> monoToExecute = mono;
		
		if (monoToExecute != null) {
			Runnable r = () -> monoToExecute.subscribe(this::subscriberToSendNotification); 
			new Thread(r).start();
		}

		return Mono.just(new ResponseEntity<Void>(HttpStatus.OK));
	}
	
	private void subscriberToSendNotification(boolean status) {
		_LOGGER.debug(status ? "Notification sent" : "Notification not sent");
	}

	
}
