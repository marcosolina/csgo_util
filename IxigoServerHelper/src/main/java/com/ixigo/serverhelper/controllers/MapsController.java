package com.ixigo.serverhelper.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ixigo.library.mediators.web.interfaces.WebMediator;
import com.ixigo.serverhelper.commands.GetMapsCmd;
import com.ixigo.serverhelper.models.rest.RestServerMaps;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/maps")
public class MapsController {
	private static final Logger LOGGER = LoggerFactory.getLogger(MapsController.class);
	@Autowired
	private WebMediator mediator;

	@GetMapping
	public Mono<ResponseEntity<RestServerMaps>> getMaps() {
		LOGGER.trace("Inside MapsController.getMaps");
		return mediator.send(new GetMapsCmd());
	}
}
