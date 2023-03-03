package com.ixigo.ui.controllers;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ixigo.library.rest.interfaces.IxigoWebClientUtils;
import com.ixigo.ui.config.IxigoEndPoints;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping(Forward.REQUEST_MAPPING + "/**")
public class Forward {
	public static final String REQUEST_MAPPING = "/services";
	@Autowired
	private IxigoWebClientUtils webClient;
	@Autowired
	private IxigoEndPoints endPoints;

	@GetMapping
	@PostMapping
	@DeleteMapping
	@PutMapping
	// @formatter:off
	public Mono<ResponseEntity<Object>> get(
			HttpServletRequest request,
			@RequestParam Map<String,String> allRequestParams,
			@RequestBody(required = false) Object body) throws MalformedURLException {
	// @formatter:on
		var url = request.getRequestURI().replace(request.getContextPath() + REQUEST_MAPPING, endPoints.getEndPoints().get("gateway").get("base-url"));
		HttpMethod method = HttpMethod.resolve(request.getMethod());
		var tmpBody = Serializable.class.cast(body);
		return webClient.performRequest(Object.class, method, new URL(url), Optional.empty(), Optional.of(allRequestParams), Optional.empty(), Optional.ofNullable(tmpBody));
	}
}
