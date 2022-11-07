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
import com.ixigo.eventdispatcher.models.rest.EventListenerResgistraionRequest;
import com.ixigo.integrationtests.components.SharedResponseEntity;
import com.ixigo.integrationtests.configuration.properties.EventDispatcherEndPoints;
import com.ixigo.library.rest.interfaces.IxigoWebClientUtils;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;

public class EventListenerSteps {
	private static final Logger _LOGGER = LoggerFactory.getLogger(EventListenerSteps.class);
	@Autowired
	private SharedResponseEntity sharedCr;
	@Autowired
	private EventDispatcherEndPoints endPoints;
	@Autowired
	private IxigoWebClientUtils webClient;

	private EventListenerResgistraionRequest listener = new EventListenerResgistraionRequest();
	{
		listener.setEventType(EventType.WARMUP_START);
		listener.setUrl("https://marco.selfip.net");
	}

	@Given("I dont have the listener registered")
	public void i_dont_have_the_listener_registered() {
		try {
			URL url = new URL(endPoints.getDeleteListener());
			_LOGGER.debug(url.toString());
			var resp = webClient.performRequestNoExceptions(Void.class, HttpMethod.DELETE, url, Optional.empty(), Optional.empty(), Optional.empty(), Optional.of(listener)).block();
			assertNotNull(resp);
			sharedCr.setSharedResp(resp);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	@When("I perform a POST request to register a new listern")
	@When("I perform a POST request to register again the same listener")
	public void i_perform_a_post_request_to_register_a_new_listern() {
		try {
			URL url = new URL(endPoints.getPostListener());
			_LOGGER.debug(url.toString());
			var resp = webClient.performRequestNoExceptions(Void.class, HttpMethod.POST, url, Optional.empty(), Optional.empty(), Optional.empty(), Optional.of(listener)).block();
			assertNotNull(resp);
			sharedCr.setSharedResp(resp);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	@When("I perform a DELETE request to un-register the listener")
	public void i_perform_a_delete_request_to_un_register_the_listener() {
		try {
			URL url = new URL(endPoints.getDeleteListener());
			_LOGGER.debug(url.toString());
			var resp = webClient.performRequestNoExceptions(Void.class, HttpMethod.DELETE, url, Optional.empty(), Optional.empty(), Optional.empty(), Optional.of(listener)).block();
			assertNotNull(resp);
			sharedCr.setSharedResp(resp);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
}
