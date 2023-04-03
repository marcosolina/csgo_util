package com.ixigo.discordbot.handlers.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.ixigo.discordbot.commands.config.GetBotConfigAllCmd;
import com.ixigo.discordbot.mappers.RestMapper;
import com.ixigo.discordbot.services.interfaces.IxigoBot;
import com.ixigo.library.mediators.web.interfaces.WebCommandHandler;
import com.ixigo.models.rest.RestBotConfigs;

import reactor.core.publisher.Mono;

@Component
public class GetBotConfigAllCmdHandler implements WebCommandHandler<GetBotConfigAllCmd, RestBotConfigs>{
	private static final Logger _LOGGER = LoggerFactory.getLogger(GetBotConfigAllCmdHandler.class);
	@Autowired
	private IxigoBot botService;
	@Autowired
	private RestMapper mapper;
	
	@Override
	public Mono<ResponseEntity<RestBotConfigs>> handle(GetBotConfigAllCmd cmd) {
		_LOGGER.trace("Inside GetBotConfigCmdHandler.handle");
		
		// @formatter:off
		return botService.getBotConfigAll()
			.map(mapper::fromSvcToRest)
			.collectList()
			.map(configList -> {
				var resp = new RestBotConfigs();
				resp.setConfigs(configList);
				return new ResponseEntity<>(resp, HttpStatus.OK);
			});
		// @formatter:on
	}

}
