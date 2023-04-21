package com.ixigo.integrationtests.steps.demmanager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import com.ixigo.demmanagercontract.models.enums.UsersScoresQueryParam;
import com.ixigo.demmanagercontract.models.rest.demdata.RestMapStats;
import com.ixigo.demmanagercontract.models.rest.demdata.RestMapsPlayed;
import com.ixigo.demmanagercontract.models.rest.demdata.RestScoreTypes;
import com.ixigo.demmanagercontract.models.rest.demdata.RestUsers;
import com.ixigo.demmanagercontract.models.rest.demdata.RestUsersScores;
import com.ixigo.integrationtests.components.SharedResponseEntity;
import com.ixigo.integrationtests.configuration.properties.DemManagersEndPoints;
import com.ixigo.library.rest.interfaces.IxigoWebClientUtils;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class DemDataSteps {
	private static final Logger _LOGGER = LoggerFactory.getLogger(DemDataSteps.class);
	private List<String> steamIds = new ArrayList<>();
	private Integer numberOfMatches = 0;

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
	
	@When("I perform a GET request to retrieve maps played counters")
	public void i_perform_a_get_request_to_retrieve_maps_played_counters() {
		try {
			URL url = new URL(endPoints.getGetChartsDataMapsPlayed());
			_LOGGER.debug(url.toString());
			var resp = webClient.performGetRequestNoExceptions(RestMapsPlayed.class, url, Optional.empty(), Optional.empty()).block();
			assertNotNull(resp);
			sharedCr.setSharedResp(resp);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	@Then("I should receive {int} maps in the response")
	public void i_should_receive_maps_in_the_response(Integer count) {
		ResponseEntity<RestMapsPlayed> resp = sharedCr.getSharedResp(RestMapsPlayed.class);
		assertNotNull(resp);
		assertNotNull(resp.getBody());
		assertNotNull(resp.getBody().getMaps());
		assertEquals(count, resp.getBody().getMaps().size());
	}
	
	@Then("the map list should contain the following maps:")
	public void the_map_list_should_contain_the_following_maps(DataTable dataTable) {
		ResponseEntity<RestMapsPlayed> resp = sharedCr.getSharedResp(RestMapsPlayed.class);
		RestMapsPlayed mapsPlayed = resp.getBody();
		dataTable.asList().forEach(mapName -> {
			assertEquals(1, mapsPlayed.getMaps().stream().filter(m -> m.getMapName().equals(mapName)).count());
		});
	}
	
	@When("I perform a GET request to retrieve the list of users")
	public void i_perform_a_get_request_to_retrieve_the_list_of_users() {
		try {
			URL url = new URL(endPoints.getGetDemDataUsers());
			_LOGGER.debug(url.toString());
			var resp = webClient.performGetRequestNoExceptions(RestUsers.class, url, Optional.empty(), Optional.empty()).block();
			assertNotNull(resp);
			sharedCr.setSharedResp(resp);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	@Then("I should receive {int} users in the response")
	public void i_should_receive_users_in_the_response(Integer count) {
		ResponseEntity<RestUsers> resp = sharedCr.getSharedResp(RestUsers.class);
		assertNotNull(resp);
		assertNotNull(resp.getBody());
		assertNotNull(resp.getBody().getUsers());
		assertEquals(count, resp.getBody().getUsers().size());
	}
	@Then("the users list should contain the following names:")
	public void the_users_list_should_contain_the_following_names(DataTable dataTable) {
		ResponseEntity<RestUsers> resp = sharedCr.getSharedResp(RestUsers.class);
		RestUsers users = resp.getBody();
		dataTable.asList().forEach(userName -> {
			assertEquals(1, users.getUsers().stream().filter(m -> m.getUserName().equals(userName)).count());
		});
	}
	
	@Given("that I want to retrieve the scores for the last {int} matches for the following users:")
	public void that_i_want_to_retrieve_the_scores_for_the_last_matches_for_the_following_users(Integer count, DataTable dataTable) {
		numberOfMatches = count;
		steamIds.clear();
		steamIds.addAll(dataTable.asList());
	}
	@Then("I perform a GET request to retrieve the users score")
	public void i_perform_a_get_request_to_retrieve_the_users_score() {
		try {
			URL url = new URL(endPoints.getGetDemDataUsersScores());
			_LOGGER.debug(url.toString());
			Map<String, String> queryParams = new HashMap<>();
			queryParams.put(UsersScoresQueryParam.NUMBER_OF_MATCHES.getQueryParamKey(), numberOfMatches.toString());
			queryParams.put(UsersScoresQueryParam.USERS_STEAM_IDS.getQueryParamKey(), String.join(",", steamIds));
			var resp = webClient.performGetRequestNoExceptions(RestUsersScores.class, url, Optional.empty(), Optional.of(queryParams)).block();
			assertNotNull(resp);
			sharedCr.setSharedResp(resp);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	@Then("I should have the following data in the response body")
	public void i_should_have_the_following_data_in_the_response_body(DataTable dataTable) {
		ResponseEntity<RestUsersScores> resp = sharedCr.getSharedResp(RestUsersScores.class);
		assertNotNull(resp);
		assertNotNull(resp.getBody());
		
		Map<String, List<RestMapStats>> usersScores = resp.getBody().getUsersScores();
		
		List<Map<String, String>> rows = dataTable.asMaps();
		rows.forEach(row -> {
			List<RestMapStats> listStats = usersScores.get(row.get("steamId"));
			assertNotNull(listStats);
			assertEquals(Integer.parseInt(row.get("number_of_matches_founded")), listStats.size());
		});
	}
}


