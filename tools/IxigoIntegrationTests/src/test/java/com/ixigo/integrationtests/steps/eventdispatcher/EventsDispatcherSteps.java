package com.ixigo.integrationtests.steps.eventdispatcher;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

import com.ixigo.eventdispatcher.enums.EventType;
import com.ixigo.eventdispatcher.models.rest.IncomingEventHttpRequest;
import com.ixigo.integrationtests.components.SharedResponseEntity;
import com.ixigo.integrationtests.configuration.properties.EventDispatcherEndPoints;
import com.ixigo.library.rest.interfaces.IxigoWebClientUtils;

import io.cucumber.java.en.When;

public class EventsDispatcherSteps {
	private static final Logger _LOGGER = LoggerFactory.getLogger(EventsDispatcherSteps.class);
	@Autowired
	private SharedResponseEntity sharedCr;
	@Autowired
	private EventDispatcherEndPoints endPoints;
	@Autowired
	private IxigoWebClientUtils webClient;
	
	private IncomingEventHttpRequest event = new IncomingEventHttpRequest();
	{
		event.setEventName(EventType.WARMUP_START.getDesc());
	}
	
	@When("I perform a POST request to dispatch a new event")
	public void i_perform_a_post_request_to_dispatch_a_new_event() {
		try {
			URL url = new URL(endPoints.getPostEvent());
			_LOGGER.debug(url.toString());
			var resp = webClient.performRequestNoExceptions(Void.class, HttpMethod.POST, url, Optional.empty(), Optional.empty(), Optional.empty(), Optional.of(event)).block();
			assertNotNull(resp);
			sharedCr.setSharedResp(resp);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	@When("I dispatch a round_announce_warmup")
	public void i_dispatch_a_round_announce_warmup() {
		try {
			var event = new IncomingEventHttpRequest();
			event.setEventName(EventType.WARMUP_START.getDesc());
			
			URL url = new URL(endPoints.getPostEvent());
			_LOGGER.debug(url.toString());
			var resp = webClient.performRequestNoExceptions(Void.class, HttpMethod.POST, url, Optional.empty(), Optional.empty(), Optional.empty(), Optional.of(event)).block();
			assertNotNull(resp);
			sharedCr.setSharedResp(resp);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
}
