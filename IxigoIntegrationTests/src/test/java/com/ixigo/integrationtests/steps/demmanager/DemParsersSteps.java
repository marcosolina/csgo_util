package com.ixigo.integrationtests.steps.demmanager;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

import com.ixigo.integrationtests.components.SharedResponseEntity;
import com.ixigo.integrationtests.configuration.properties.DemManagersEndPoints;
import com.ixigo.library.rest.interfaces.IxigoWebClientUtils;

import io.cucumber.java.en.When;

public class DemParsersSteps {
	private static final Logger _LOGGER = LoggerFactory.getLogger(DemParsersSteps.class);
	@Autowired
	private SharedResponseEntity sharedCr;
	@Autowired
	private DemManagersEndPoints endPoints;

	@Autowired
	private IxigoWebClientUtils webClient;

	@When("I perform a POST request to parse the queued files")
	public void i_perform_a_post_request_to_parse_the_queued_files() {
		try {
			URL url = new URL(endPoints.getPostParseQueuedFiles());
			_LOGGER.debug(url.toString());
			var resp = webClient.performRequestNoExceptions(Void.class, HttpMethod.POST, url, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty()).block();
			assertNotNull(resp);
			sharedCr.setSharedResp(resp);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	@When("I perform a POST request to parse the queued and non queued files")
	public void i_perform_a_post_request_to_parse_the_queued_and_non_queued_files() {
		try {
			URL url = new URL(endPoints.getPostParseAllFiles());
			_LOGGER.debug(url.toString());
			var resp = webClient.performRequestNoExceptions(Void.class, HttpMethod.POST, url, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty()).block();
			assertNotNull(resp);
			sharedCr.setSharedResp(resp);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
}
