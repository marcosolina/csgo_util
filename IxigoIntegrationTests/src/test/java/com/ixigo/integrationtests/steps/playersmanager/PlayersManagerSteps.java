package com.ixigo.integrationtests.steps.playersmanager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import com.ixigo.integrationtests.components.SharedResponseEntity;
import com.ixigo.integrationtests.configuration.properties.PlayersManagerEndPoints;
import com.ixigo.library.rest.interfaces.IxigoWebClientUtils;
import com.ixigo.playersmanagercontract.enums.GetTeamsQueryParam;
import com.ixigo.playersmanagercontract.models.rest.RestTeams;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class PlayersManagerSteps {
	private static final Logger _LOGGER = LoggerFactory.getLogger(PlayersManagerSteps.class);
	private List<String> steamIds = new ArrayList<>();
	
	@Autowired
	private SharedResponseEntity sharedCr;
	@Autowired
	private PlayersManagerEndPoints endPoints;
	@Autowired
	private IxigoWebClientUtils webClient;
	
	
	@Given("that I want to split the following players into two teams")
	public void that_i_want_to_split_the_following_players_into_two_teams(DataTable dataTable) {
		List<Map<String, String>> rows = dataTable.asMaps();
		steamIds.clear();
		rows.forEach(row -> steamIds.add(row.get("steamId")));
	}
	@When("I perform a GET request to the players manager with the following query parameters")
	public void i_perform_a_get_request_to_the_players_manager_with_the_following_query_parameters(DataTable dataTable) {
		try {
			URL url = new URL(endPoints.getGetTeams());
			_LOGGER.debug(url.toString());
			Map<String, String> queryParams = new HashMap<>();
			
			List<Map<String, String>> rows = dataTable.asMaps();
			assertEquals(1, rows.size());
			
			var map = rows.get(0);
			queryParams.put(GetTeamsQueryParam.NUMBER_OF_MATCHES.getQueryParamKey(), map.get("numberOfMatches"));
			queryParams.put(GetTeamsQueryParam.USERS_STEAM_IDS.getQueryParamKey(), String.join(",", steamIds));
			queryParams.put(GetTeamsQueryParam.MINIMUM_PERC_MATCH_PLAYED.getQueryParamKey(), map.get("minPercPlayed"));
			queryParams.put(GetTeamsQueryParam.PENALTY_WEIGHT.getQueryParamKey(), map.get("penaltyWeigth"));
			queryParams.put(GetTeamsQueryParam.PARTITION_SCORE_TYPE.getQueryParamKey(), map.get("partitionScore"));
			
			var resp = webClient.performGetRequestNoExceptions(RestTeams.class, url, Optional.empty(), Optional.of(queryParams)).block();
			assertNotNull(resp);
			sharedCr.setSharedResp(resp);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	@Then("the two teams shold have the following specs")
	public void the_two_teams_shold_have_the_following_specs(DataTable dataTable) {
		ResponseEntity<RestTeams> resp = sharedCr.getSharedResp(RestTeams.class);
		assertNotNull(resp);
		assertNotNull(resp.getBody());
		
		var teams = resp.getBody().getTeams();
		List<Map<String, String>> rows = dataTable.asMaps();
		
		for(int i = 0; i < rows.size(); i++) {
			var row = rows.get(i);
			var team = teams.get(i);
			assertEquals(new BigDecimal(row.get("teamScore")), team.getTeamScore());
		}
	}
	@Then("the players of the teams should be")
	public void the_players_of_the_teams_should_be(DataTable dataTable) {
		ResponseEntity<RestTeams> resp = sharedCr.getSharedResp(RestTeams.class);
		
		var teams = resp.getBody().getTeams();
		var team1 = teams.get(0).getMembers().stream().map(m -> m.getSteamID()).collect(Collectors.toList());
		var team2 = teams.get(1).getMembers().stream().map(m -> m.getSteamID()).collect(Collectors.toList());
		
		List<Map<String, String>> rows = dataTable.asMaps();
		rows.forEach(row -> {
			var playerTeam1 = row.get("team_one_players");
			var playerTeam2 = row.get("team_two_players");
			
			if(!playerTeam1.equals("0")) {
				assertTrue(team1.contains(playerTeam1));
			}
			
			if(!playerTeam2.equals("0")) {
				assertTrue(team2.contains(playerTeam2));
			}
		});
	}
}
