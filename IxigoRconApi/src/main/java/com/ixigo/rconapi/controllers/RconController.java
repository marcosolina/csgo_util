package com.ixigo.rconapi.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ixigo.library.mediators.web.interfaces.WebMediator;
import com.ixigo.rconapi.commands.PostRconCmd;
import com.ixigo.rconapi.models.rest.RestRconRequest;
import com.ixigo.rconapi.models.rest.RestRconResponse;

import io.swagger.annotations.ApiOperation;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/cmd")
public class RconController {
	private static final Logger LOGGER = LoggerFactory.getLogger(RconController.class);
	@Autowired
	private WebMediator mediator;

	@PostMapping
	@ApiOperation(value = "It sends the command to the specific rcon server", code = 200)
	public Mono<ResponseEntity<RestRconResponse>> postRconCommand(@RequestBody RestRconRequest request) {
		LOGGER.trace("Inside RconController.postRconCommand");
		return mediator.send(new PostRconCmd(request));
	}
}
