package com.ixigo.serverhelper.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ixigo.library.mediators.web.interfaces.WebMediator;
import com.ixigo.serverhelper.commands.GetCsgoConfigCmd;
import com.ixigo.serverhelper.models.rest.RestCsgoConfig;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/config")
public class ConfigController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ConfigController.class);
	@Autowired
	private WebMediator mediator;

	@GetMapping("/csgo")
	public Mono<ResponseEntity<RestCsgoConfig>> getCsgoConfig() {
		LOGGER.trace("Inside ConfigController.getCsgoConfig");
		return mediator.send(new GetCsgoConfigCmd());
	}
}
