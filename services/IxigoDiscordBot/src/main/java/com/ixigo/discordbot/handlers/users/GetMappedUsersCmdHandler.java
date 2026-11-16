package com.ixigo.discordbot.handlers.users;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.ixigo.discordbot.commands.users.GetMappedUsersCmd;
import com.ixigo.discordbot.mappers.RestMapper;
import com.ixigo.discordbot.services.interfaces.IxigoBot;
import com.ixigo.library.mediators.web.interfaces.WebCommandHandler;
import com.ixigo.models.rest.RestMappedPlayers;

import reactor.core.publisher.Mono;

@Component
public class GetMappedUsersCmdHandler implements WebCommandHandler<GetMappedUsersCmd, RestMappedPlayers>{
	private static final Logger _LOGGER = LoggerFactory.getLogger(GetMappedUsersCmdHandler.class);
	@Autowired
	private IxigoBot botService;
	@Autowired
	private RestMapper mapper;
	
	@Override
	public Mono<ResponseEntity<RestMappedPlayers>> handle(GetMappedUsersCmd cmd) {
		_LOGGER.trace("Inside GetMappedUsersCmdHandler.handle");
		// @formatter:off
		return botService.getListOfMappedPlayers()
			.map(mapper::fromSvcToRest)
			.collectList()
			.map(list -> new RestMappedPlayers(list))
			.map(resp -> new ResponseEntity<>(resp, HttpStatus.OK))
			;
		// @formatter:on
	}

}
