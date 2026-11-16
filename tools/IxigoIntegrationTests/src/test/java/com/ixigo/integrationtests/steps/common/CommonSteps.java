package com.ixigo.integrationtests.steps.common;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import com.ixigo.integrationtests.components.SharedResponseEntity;

import io.cucumber.java.en.And;

public class CommonSteps {
	@Autowired
	private SharedResponseEntity sharedCr;
	
	@And("I should receive a {int} status in the response")
	public void i_should_receive_a_status_in_the_response(Integer int1) {
		assertNotNull(sharedCr.getSharedResp(Object.class));
		assertEquals(HttpStatus.valueOf(int1), sharedCr.getSharedResp(Object.class).getStatusCode());
	}
	
	@And("I give {int} seconds to the server to process the dem files")
	public void i_give_seconds_to_the_server_to_process_the_dem_files(Integer seconds) {
		try {
			Thread.sleep(seconds * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
