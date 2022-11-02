package com.ixigo.integrationtests.steps.demmanager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ResourceUtils;
import org.springframework.web.util.UriBuilder;

import com.ixigo.integrationtests.components.SharedResponseEntity;
import com.ixigo.integrationtests.configuration.properties.DemManagersEndPoints;
import com.ixigo.integrationtests.constants.DemFiles;
import com.ixigo.library.rest.interfaces.IxigoWebClientUtils;

import io.cucumber.java.en.Given;

public class DemBackgrounds {
	private static final Logger _LOGGER = LoggerFactory.getLogger(DemBackgrounds.class);
	@Autowired
	private SharedResponseEntity sharedCr;
	@Autowired
	private DemManagersEndPoints endPoints;

	@Autowired
	private IxigoWebClientUtils webClient;

	@Given("that I have two DEM files")
	public void that_i_have_two_dem_files() {
		Arrays.asList(DemFiles.FOLDER + "/" + DemFiles.FILE_1, DemFiles.FOLDER + "/" + DemFiles.FILE_2)
		.forEach(f -> {
			try {
				File file = ResourceUtils.getFile(f);
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
				assertEquals(HttpStatus.CREATED, response.getStatusCode());
			} catch (IOException e) {
				e.printStackTrace();
				fail(e.getMessage());
			}
		});

	}
	
	@Given("only one of them is marked as queued")
	public void only_one_of_them_is_marked_as_queued() {
		try {
			URL url = new URL(endPoints.getDeleteDemFileFromQueue(DemFiles.FILE_2));
			_LOGGER.debug(url.toString());
			var resp = webClient.performRequestNoExceptions(Void.class, HttpMethod.DELETE, url, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty()).block();
			assertNotNull(resp);
			assertEquals(HttpStatus.OK, resp.getStatusCode());
		} catch (MalformedURLException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	@Given("they are both parsed")
	public void they_are_both_parsed() {
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
