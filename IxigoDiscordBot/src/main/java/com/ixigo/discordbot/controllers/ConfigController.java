package com.ixigo.discordbot.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ixigo.discordbot.commands.config.GetBotConfigCmd;
import com.ixigo.discordbot.commands.config.PutBotConfigCmd;
import com.ixigo.enums.BotConfigKey;
import com.ixigo.library.mediators.web.interfaces.WebMediator;
import com.ixigo.models.rest.RestBotConfig;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/config")
public class ConfigController {
	private static final Logger _LOGGER = LoggerFactory.getLogger(ConfigController.class);

	@Autowired
	private WebMediator mediator;

	@GetMapping(value = "/{key}")
	public Mono<ResponseEntity<RestBotConfig>> getConfig(@PathVariable("key") BotConfigKey key) {
		_LOGGER.trace("Inside ConfigController.getConfig");
		return mediator.send(new GetBotConfigCmd(key));
	}
	
	@PutMapping
	public Mono<ResponseEntity<RestBotConfig>> putConfig(@RequestBody RestBotConfig config) {
		_LOGGER.trace("Inside ConfigController.putConfig");
		return mediator.send(new PutBotConfigCmd(config));
	}
}
