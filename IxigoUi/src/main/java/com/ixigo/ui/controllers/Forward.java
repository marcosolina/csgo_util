package com.ixigo.ui.controllers;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.ixigo.library.mediators.web.interfaces.WebMediator;
import com.ixigo.ui.commands.ForwardHttpReqCmd;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping(Forward.REQUEST_MAPPING + "/**")
public class Forward {
	private static final Logger _LOGGER = LoggerFactory.getLogger(Forward.class);
	public static final String REQUEST_MAPPING = "/services";

	@Autowired
	private WebMediator mediator;

	@GetMapping
	@PostMapping
	@DeleteMapping
	@PutMapping
	// @formatter:off
	public Mono<ResponseEntity<Object>> forwardHttpRequest(
			HttpServletRequest request,
			@RequestParam Map<String,String> allRequestParams,
			@RequestBody(required = false) Object body){
		
		_LOGGER.trace("Inside Forward.forwardHttpRequest");
		
		return mediator.send(
			ForwardHttpReqCmd.builder()
			.contextPath(request.getContextPath())
			.body(body)
			.httpMethod(HttpMethod.resolve(request.getMethod()))
			.queryParams(allRequestParams)
			.requestedUri(request.getRequestURI())
			.build()
		);
	}
	// @formatter:on
}
