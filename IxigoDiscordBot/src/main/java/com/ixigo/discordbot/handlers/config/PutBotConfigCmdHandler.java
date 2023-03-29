package com.ixigo.discordbot.handlers.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.ixigo.discordbot.commands.config.PutBotConfigCmd;
import com.ixigo.discordbot.mappers.RestMapper;
import com.ixigo.discordbot.services.interfaces.IxigoBot;
import com.ixigo.library.mediators.web.interfaces.WebCommandHandler;

import reactor.core.publisher.Mono;

@Component
public class PutBotConfigCmdHandler implements WebCommandHandler<PutBotConfigCmd, Void> {
	private static final Logger _LOGGER = LoggerFactory.getLogger(PutBotConfigCmdHandler.class);
	@Autowired
	private IxigoBot botService;
	@Autowired
	private RestMapper mapper;

	@Override
	public Mono<ResponseEntity<Void>> handle(PutBotConfigCmd cmd) {
		_LOGGER.trace("Inside PutBotConfigCmdHandler.handle");

		// @formatter:off
		return botService.updateBotConfig(mapper.fromRestToSvc(cmd.getConfig()))
			.map(status -> new ResponseEntity<>(status ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR));
		// @formatter:on
	}

}
