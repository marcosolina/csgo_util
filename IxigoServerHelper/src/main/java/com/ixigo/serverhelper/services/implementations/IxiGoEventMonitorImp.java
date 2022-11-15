package com.ixigo.serverhelper.services.implementations;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Scheduled;

import com.ixigo.eventdispatcher.models.rest.IncomingEventHttpRequest;
import com.ixigo.library.rest.interfaces.IxigoWebClientUtils;
import com.ixigo.serverhelper.config.properties.EventDispatcherEndPoints;
import com.ixigo.serverhelper.config.properties.EventProperties;
import com.ixigo.serverhelper.services.interfaces.IxiGoEventMonitor;

public class IxiGoEventMonitorImp implements IxiGoEventMonitor {
	private static final Logger _LOGGER = LoggerFactory.getLogger(IxiGoEventMonitorImp.class);
	
	@Autowired
	private EventProperties eventProps;
	@Autowired
	private EventDispatcherEndPoints eventDispatcherProps;
	@Autowired
	private IxigoWebClientUtils webClient;
	
	private String oldValue = "NO";

	@Override
	@Scheduled(cron = "*/1 * * * * *") // Every second
	public void scheduledCheckForNewEvent() {
		_LOGGER.trace("Checking event file");
		if(!eventProps.getSendEvents()) {
			return;
		}
		
		String currentValue = readFile();
        _LOGGER.trace("Current read value: " + currentValue);
        if(currentValue != null && !oldValue.equals(currentValue)) {
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
	
	private String readFile() {
        String cmd = String.format(
                "tail %s/csgo/addons/sourcemod/event.txt",
                System.getenv().get("ENV_CSGO_INSTALL_FOLDER"));
        _LOGGER.trace(cmd);

        try {
            String err = null;
            String input = null;
            Process p = Runtime.getRuntime().exec(cmd);
            p.waitFor();
            
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            StringBuilder sbError = new StringBuilder();
            StringBuilder sbInput = new StringBuilder();
            
            while ((err = stdError.readLine()) != null) {
                sbError.append(err);
            }
            
            while ((input = stdInput.readLine()) != null) {
                sbInput.append(input);
            }
            
            if (sbError.length() == 0) {
                return sbInput.toString();
            }else {
                _LOGGER.error(sbError.toString());
            }
        } catch (IOException | InterruptedException e) {
            _LOGGER.error(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

}
