package com.ixigo.serverhelper.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.ixigo.library.mediators.web.interfaces.WebCommandHandler;
import com.ixigo.serverhelper.commands.GetCsgoConfigCmd;
import com.ixigo.serverhelper.mappers.RestMapper;
import com.ixigo.serverhelper.models.rest.RestCsgoConfig;
import com.ixigo.serverhelper.services.interfaces.CsgoServerConfigManager;

import reactor.core.publisher.Mono;

@Component
public class GetCsgoConfigCmdHandler implements WebCommandHandler<GetCsgoConfigCmd, RestCsgoConfig>{
	private static final Logger LOGGER = LoggerFactory.getLogger(GetCsgoConfigCmdHandler.class);
	
	@Autowired
	private CsgoServerConfigManager service;
	@Autowired
	private RestMapper mapper;
	
	@Override
	public Mono<ResponseEntity<RestCsgoConfig>> handle(GetCsgoConfigCmd cmd) {
		LOGGER.trace("Inside GetCsgoConfigCmdHandler.handle");
		// @formatter:off
		return service.getServerConfig()
				.map(mapper::fromSvcToRest)
				.map(resp -> new ResponseEntity<>(resp, HttpStatus.OK));
		// @formatter:on
	}

}
