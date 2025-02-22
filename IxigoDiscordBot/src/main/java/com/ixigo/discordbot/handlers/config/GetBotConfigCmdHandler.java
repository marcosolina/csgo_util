package com.ixigo.discordbot.handlers.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.ixigo.discordbot.commands.config.GetBotConfigCmd;
import com.ixigo.discordbot.mappers.RestMapper;
import com.ixigo.discordbot.services.interfaces.IxigoBot;
import com.ixigo.library.mediators.web.interfaces.WebCommandHandler;
import com.ixigo.models.rest.RestBotConfig;

import reactor.core.publisher.Mono;

@Component
public class GetBotConfigCmdHandler implements WebCommandHandler<GetBotConfigCmd, RestBotConfig>{
	private static final Logger _LOGGER = LoggerFactory.getLogger(GetBotConfigCmdHandler.class);
	@Autowired
	private IxigoBot botService;
	@Autowired
	private RestMapper mapper;
	
	@Override
	public Mono<ResponseEntity<RestBotConfig>> handle(GetBotConfigCmd cmd) {
		_LOGGER.trace("Inside GetBotConfigCmdHandler.handle");
		
		// @formatter:off
		return botService.getBotConfig(cmd.getKey())
			.map(mapper::fromSvcToRest)
			.map(rest -> new ResponseEntity<>(rest, HttpStatus.OK))
		;
		// @formatter:on
	}

}
