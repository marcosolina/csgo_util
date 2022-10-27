package com.ixigo.integrationtests.steps.demmanager;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ResourceUtils;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.util.UriBuilder;

import com.ixigo.integrationtests.components.SharedClientResponse;
import com.ixigo.integrationtests.configuration.properties.DemManagersEndPoints;
import com.ixigo.library.rest.interfaces.IxigoWebClientUtils;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import reactor.core.publisher.Mono;

public class DemManagerSteps {
	private static String fileName = "auto0-20221024-194632-1820591623-de_inferno-IXI-GO__Monday_Nights.dem";
	private static final Logger _LOGGER = LoggerFactory.getLogger(DemManagerSteps.class);
	
	@Autowired
	private SharedClientResponse sharedCr;
	@Autowired
	private DemManagersEndPoints endPoints;
	
	
	@Autowired
	private IxigoWebClientUtils webClient;

	@Given("I have a new DEM file")
	public void i_have_a_new_dem_file() {
		try {
			assertTrue(ResourceUtils.getFile("classpath:dem/" + fileName).exists());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			fail("No dem file");
		}
	}

	@Then("I POST the file to the service")
	public void i_post_the_file_to_the_service() {
		
		try {
			File file = ResourceUtils.getFile("classpath:dem/" + fileName);
            Resource resource = new UrlResource(file.toURI());

            MultipartBodyBuilder builder = new MultipartBodyBuilder();
            builder.part("file", resource).filename(file.getName());

            MultiValueMap<String, HttpEntity<?>> parts = builder.build();

            URL url = new URL(endPoints.getPostDemFile());
            _LOGGER.debug(url.toString());
            
            // @formatter:off
            Mono<ClientResponse> response = webClient.getWebBuilder().build().post().uri(uriBuilder -> {
                    UriBuilder ub = uriBuilder
                            .scheme(url.getProtocol())
                            .host(url.getHost())
                            .port(url.getPort())
                            .path(url.getPath());
                    return ub.build();
    
                })
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .bodyValue(parts)
                .exchangeToMono(Mono::just);
            // @formatter:on
            sharedCr.setSharedResp(response);
            assertNotNull(sharedCr.getSharedResp());
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
	}
}
