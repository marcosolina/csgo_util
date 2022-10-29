package com.ixigo.integrationtests.steps.demmanager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ResourceUtils;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.util.UriBuilder;

import com.ixigo.demmanagercontract.models.rest.demfilesmanager.RestGetFilesResponse;
import com.ixigo.integrationtests.components.SharedResponseEntity;
import com.ixigo.integrationtests.configuration.properties.DemManagersEndPoints;
import com.ixigo.library.rest.interfaces.IxigoWebClientUtils;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class DemManagerSteps {
	private static String fileName = "auto0-20221024-194632-1820591623-de_inferno-IXI-GO__Monday_Nights.dem";
	private static String demFileFolder = "classpath:dem/";
	private static final Logger _LOGGER = LoggerFactory.getLogger(DemManagerSteps.class);

	@Autowired
	private SharedResponseEntity sharedCr;
	@Autowired
	private DemManagersEndPoints endPoints;

	@Autowired
	private IxigoWebClientUtils webClient;

	@Given("I have a new DEM file")
	public void i_have_a_new_dem_file() {
		try {
			assertTrue(ResourceUtils.getFile(demFileFolder + fileName).exists());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			fail("No dem file");
		}
	}

	@Then("I POST the file to the service")
	public void i_post_the_file_to_the_service() {

		try {
			File file = ResourceUtils.getFile(demFileFolder + fileName);
			Resource resource = new UrlResource(file.toURI());

			MultipartBodyBuilder builder = new MultipartBodyBuilder();
			builder.part("file", resource).filename(file.getName());

			MultiValueMap<String, HttpEntity<?>> parts = builder.build();

			URL url = new URL(endPoints.getPostDemFile());
			_LOGGER.debug(url.toString());

			// @formatter:off
            var response = webClient.getWebBuilder().build().post().uri(uriBuilder -> {
                    UriBuilder ub = uriBuilder
                            .scheme(url.getProtocol())
                            .host(url.getHost())
                            .port(url.getPort())
                            .path(url.getPath());
                    return ub.build();
    
                })
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .bodyValue(parts)
                .retrieve()
                .toEntity(Void.class)
                .block();
            // @formatter:on
			assertNotNull(response);
			sharedCr.setSharedResp(response);
		} catch (IOException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@When("I perform a GET with the filename")
	public void i_perform_a_get_with_the_filename() {
		try {
			URL url = new URL(endPoints.getGetDemFile(fileName));
			_LOGGER.debug(url.toString());

			final int size = 16 * 1024 * 1024 * 100;
			final ExchangeStrategies strategies = ExchangeStrategies.builder().codecs(codecs -> codecs.defaultCodecs().maxInMemorySize(size)).build();

			// @formatter:off
            
            ResponseEntity<byte[]> resp = webClient.getWebBuilder()
            		.exchangeStrategies(strategies)
            		.build()
            		.get()
            		.uri(uriBuilder -> {
	                    UriBuilder ub = uriBuilder
	                            .scheme(url.getProtocol())
	                            .host(url.getHost())
	                            .port(url.getPort())
	                            .path(url.getPath());
	                    return ub.build();
                })
            	    .retrieve()
            	    .toEntity(byte[].class).block();

            
            // @formatter:on
			assertNotNull(resp);
			sharedCr.setSharedResp(resp);
		} catch (IOException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Then("I should have the file in the payload")
	public void i_should_have_the_file_in_the_payload() {
		try {
			ResponseEntity<byte[]> response = sharedCr.getSharedResp(byte[].class);
			byte[] bytes = response.getBody();
			assertEquals(Files.readAllBytes(ResourceUtils.getFile(demFileFolder + fileName).toPath()).length, bytes.length);
		} catch (IOException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@When("That I perform a GET to the dem files manager")
	public void that_i_perform_a_get_to_the_dem_files_manager() {
		try {
			URL url = new URL(endPoints.getGetAllDemFiles());
			_LOGGER.debug(url.toString());
			var resp = webClient.performGetRequestNoExceptions(RestGetFilesResponse.class, url, Optional.empty(), Optional.empty()).block();
			assertNotNull(resp);
			sharedCr.setSharedResp(resp);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Then("I should have multiple files in the payload")
	public void i_should_have_multiple_files_in_the_payload() {
		var resp = sharedCr.getSharedResp(RestGetFilesResponse.class);
		assertNotNull(resp.getBody());
		assertNotNull(resp.getBody().getFiles());
		assertNotEquals(0, resp.getBody().getFiles().size());
	}

	@When("I perform a DELETE to the dem files manager")
	@Then("I perform another DELETE to the dem files manager")
	public void i_perform_a_delete_to_the_dem_files_manager() {
		try {
			URL url = new URL(endPoints.getDeleteDemFileFromQueue(fileName));
			_LOGGER.debug(url.toString());
			var resp = webClient.performRequestNoExceptions(Void.class, HttpMethod.DELETE, url, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty()).block();
			assertNotNull(resp);
			sharedCr.setSharedResp(resp);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
}
