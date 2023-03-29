package com.ixigo.discordbot.handlers.users;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.ixigo.discordbot.commands.users.GetDiscordUsersCmd;
import com.ixigo.discordbot.mappers.RestMapper;
import com.ixigo.discordbot.services.interfaces.IxigoBot;
import com.ixigo.library.mediators.web.interfaces.WebCommandHandler;
import com.ixigo.models.rest.RestDiscordUsers;

import reactor.core.publisher.Mono;

@Component
public class GetDiscordUsersCmdHandler implements WebCommandHandler<GetDiscordUsersCmd, RestDiscordUsers> {
	private static final Logger _LOGGER = LoggerFactory.getLogger(GetDiscordUsersCmdHandler.class);
	@Autowired
	private IxigoBot botService;
	@Autowired
	private RestMapper mapper;

	@Override
	public Mono<ResponseEntity<RestDiscordUsers>> handle(GetDiscordUsersCmd cmd) {
		_LOGGER.trace("Inside GetDiscordUsersCmdHandler.handle");

		// @formatter:off
		var mono = botService.getDiscordUsers()
			.map(mapper::fromSvcToRest)
			.collectList()
			.map(list -> new RestDiscordUsers(list))
			.map(resp -> new ResponseEntity<>(resp, HttpStatus.OK))
			;
		// @formatter:on

		return mono;
	}
}
