package com.ixigo.discordbot.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ixigo.discordbot.commands.users.GetDiscordUsersCmd;
import com.ixigo.discordbot.commands.users.GetMappedUsersCmd;
import com.ixigo.discordbot.commands.users.PutMappedUsersCmd;
import com.ixigo.library.mediators.web.interfaces.WebMediator;
import com.ixigo.models.rest.RestDiscordUsers;
import com.ixigo.models.rest.RestMappedPlayers;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/users")
public class UsersController {
	private static final Logger _LOGGER = LoggerFactory.getLogger(UsersController.class);

	@Autowired
	private WebMediator mediator;
	
	@GetMapping(value = "/mapping")
	public Mono<ResponseEntity<RestMappedPlayers>> getMappedPlayersList() {
		_LOGGER.trace("Inside UsersController.getMappedPlayersList");
		return mediator.send(new GetMappedUsersCmd());
	}

	@PutMapping(value = "/mapping")
	public Mono<ResponseEntity<Void>> saveMappedPlayersList(@RequestBody RestMappedPlayers players) {
		_LOGGER.trace("Inside UsersController.saveMappedPlayersList");
		return mediator.send(new PutMappedUsersCmd(players));
	}

	@GetMapping(value = "/discord")
	public Mono<ResponseEntity<RestDiscordUsers>> getDiscordUsersList() {
		_LOGGER.trace("Inside UsersController.getDiscordUsersList");
		return mediator.send(new GetDiscordUsersCmd());
	}
}
