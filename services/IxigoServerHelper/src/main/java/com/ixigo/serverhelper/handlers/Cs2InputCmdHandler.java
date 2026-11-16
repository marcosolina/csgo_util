package com.ixigo.serverhelper.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.ixigo.library.mediators.web.interfaces.WebCommandHandler;
import com.ixigo.serverhelper.commands.Cs2InputCmd;
import com.ixigo.serverhelper.services.interfaces.Cs2SendInputs;

import reactor.core.publisher.Mono;

@Component
public class Cs2InputCmdHandler implements WebCommandHandler<Cs2InputCmd, Void> {
	private static final Logger LOGGER = LoggerFactory.getLogger(Cs2InputCmdHandler.class);

	@Autowired
	private Cs2SendInputs service;

	@Override
	public Mono<ResponseEntity<Void>> handle(Cs2InputCmd cmd) {
		LOGGER.trace("Inside Cs2InputCmdHandler.handle");
		return service.sendInput(cmd.getCs2Input())
				.map(resp -> new ResponseEntity<Void>(resp ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR));
	}

}
