package com.ixigo.integrationtests.steps.demmanager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import com.ixigo.demmanagercontract.models.rest.demdata.RestScoreTypes;
import com.ixigo.integrationtests.components.SharedResponseEntity;
import com.ixigo.integrationtests.configuration.properties.DemManagersEndPoints;
import com.ixigo.library.rest.interfaces.IxigoWebClientUtils;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class DemData {
	private static final Logger _LOGGER = LoggerFactory.getLogger(DemData.class);

	@Autowired
	private SharedResponseEntity sharedCr;
	@Autowired
	private DemManagersEndPoints endPoints;

	@Autowired
	private IxigoWebClientUtils webClient;
	
	@When("I perform a GET request to retrieve the type of available scores")
	public void i_perform_a_get_request_to_retrieve_the_type_of_available_scores() {
		try {
			URL url = new URL(endPoints.getGetDemDataScoresType());
			_LOGGER.debug(url.toString());
			var resp = webClient.performGetRequestNoExceptions(RestScoreTypes.class, url, Optional.empty(), Optional.empty()).block();
			assertNotNull(resp);
			sharedCr.setSharedResp(resp);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	@Then("I should receive {int} scores type in the response")
	public void i_should_receive_scores_type_in_the_response(Integer count) {
		ResponseEntity<RestScoreTypes> resp = sharedCr.getSharedResp(RestScoreTypes.class);
		assertNotNull(resp);
		assertNotNull(resp.getBody());
		assertNotNull(resp.getBody().getTypes());
		assertEquals(count, resp.getBody().getTypes().size());
	}
}


