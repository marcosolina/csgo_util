package com.ixigo.serverhelper.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ixigo.library.mediators.web.interfaces.WebMediator;
import com.ixigo.serverhelper.commands.Cs2InputCmd;
import com.ixigo.serverhelper.models.rest.Cs2InputModel;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/cs2input")
public class Cs2InputController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Cs2InputController.class);
	@Autowired
	private WebMediator mediator;

	@PostMapping
	public Mono<ResponseEntity<Void>> cs2SendInput(@RequestBody Cs2InputModel event) {
		LOGGER.trace("Inside Cs2InputController.cs2SendInput");
		return mediator.send(new Cs2InputCmd(event.getCs2Input()));
	}
}
