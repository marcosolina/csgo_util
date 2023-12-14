package com.ixigo.serverhelper.services.implementations;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Scheduled;

import com.ixigo.eventdispatcher.models.rest.IncomingEventHttpRequest;
import com.ixigo.library.config.properties.EventDispatcherEndPoints;
import com.ixigo.library.rest.interfaces.IxigoWebClientUtils;
import com.ixigo.serverhelper.config.properties.EventProperties;
import com.ixigo.serverhelper.services.interfaces.IxiGoEventMonitor;
import com.ixigo.serverhelper.services.interfaces.IxigoEventFileReader;

public class IxiGoEventMonitorImp implements IxiGoEventMonitor {
	private static final Logger _LOGGER = LoggerFactory.getLogger(IxiGoEventMonitorImp.class);
	
	@Autowired
	private EventProperties eventProps;
	@Autowired
	private EventDispatcherEndPoints eventDispatcherProps;
	@Autowired
	private IxigoWebClientUtils webClient;
	@Autowired
	private IxigoEventFileReader eventFileReader;
	
	private String oldValue = "NO";

	@Override
	@Scheduled(cron = "*/1 * * * * *") // Every second
	public void scheduledCheckForNewEvent() {
		_LOGGER.trace("Checking event file");
		if(!eventProps.getSendEvents()) {
			return;
		}
		
		String currentValue = eventFileReader.getCurrentValue();
        _LOGGER.trace("Current read value: " + currentValue);
        if(currentValue != null && !oldValue.equals(currentValue)) {
        	oldValue = currentValue;
        	try {
        		IncomingEventHttpRequest event = new IncomingEventHttpRequest();
        		event.setEventName(oldValue);
        		URL url = new URL(eventDispatcherProps.getPostEvent());
        		_LOGGER.debug(url.toString());
        		// @formatter:off
        		webClient.performRequestNoExceptions(
        				Void.class,
        				HttpMethod.POST,
        				url,
        				Optional.empty(),
        				Optional.empty(),
        				Optional.empty(),
        				Optional.of(event)
        				)
        		.doOnError(e -> _LOGGER.error(String.format("Error while sending event: %s error: %s", event.getEventName(), e.getMessage())))
        		.subscribe(resp -> _LOGGER.info(String.format("Event: %s sent", event.getEventName())))
        		;
        		// @formatter:on
        	} catch (MalformedURLException e) {
        		_LOGGER.error(e.getMessage());
        		e.printStackTrace();
        	}
        }
	}
}
