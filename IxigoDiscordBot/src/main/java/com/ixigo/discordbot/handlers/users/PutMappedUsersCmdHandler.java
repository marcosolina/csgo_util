package com.ixigo.discordbot.handlers.users;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.ixigo.discordbot.commands.users.PutMappedUsersCmd;
import com.ixigo.discordbot.mappers.RestMapper;
import com.ixigo.discordbot.services.interfaces.IxigoBot;
import com.ixigo.library.mediators.web.interfaces.WebCommandHandler;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class PutMappedUsersCmdHandler implements WebCommandHandler<PutMappedUsersCmd, Void> {
	private static final Logger _LOGGER = LoggerFactory.getLogger(PutMappedUsersCmdHandler.class);
	@Autowired
	private IxigoBot botService;
	@Autowired
	private RestMapper mapper;

	@Override
	public Mono<ResponseEntity<Void>> handle(PutMappedUsersCmd cmd) {
		_LOGGER.trace("Inside PutMappedUsersCmdHandler.handle");

		// @formatter:off
		var mono = Flux.fromIterable(cmd.getPlayersMap().getPlayers())
			.map(mapper::fromRestToSvc)
			.collectList()
			.flatMap(botService::storePlayersDetails)
			.map(status -> new ResponseEntity<Void>(status ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR))
		;
		// @formatter:on
		return mono;
	}
}
