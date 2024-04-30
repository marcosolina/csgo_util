package com.ixigo.demmanager.handlers.demfilesparser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.ixigo.demmanager.commands.demfilesparser.CmdProcessFailures;
import com.ixigo.demmanager.services.interfaces.DemFileParser;
import com.ixigo.library.mediators.web.interfaces.WebCommandHandler;

import reactor.core.publisher.Mono;

@Component
public class CmdProcessFailuresHandler implements WebCommandHandler<CmdProcessFailures, Void> {
	private static final Logger _LOGGER = LoggerFactory.getLogger(CmdProcessFailuresHandler.class);

	@Autowired
	private DemFileParser service;

	
	@Override
	public Mono<ResponseEntity<Void>> handle(CmdProcessFailures cmd) {
		_LOGGER.trace("Inside CmdProcessFailuresHandler.handle");

		return service.reprocessFailures().map(status -> new ResponseEntity<>(status));
	}
}
