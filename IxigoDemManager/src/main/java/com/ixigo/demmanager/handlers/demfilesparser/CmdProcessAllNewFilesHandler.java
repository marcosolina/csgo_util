package com.ixigo.demmanager.handlers.demfilesparser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.ixigo.demmanager.commands.demfilesparser.CmdProcessAllNewFiles;
import com.ixigo.demmanager.services.interfaces.DemFileParser;
import com.ixigo.library.mediators.web.interfaces.WebCommandHandler;

import reactor.core.publisher.Mono;

@Component
public class CmdProcessAllNewFilesHandler implements WebCommandHandler<CmdProcessAllNewFiles, Void> {
	private static final Logger _LOGGER = LoggerFactory.getLogger(CmdProcessAllNewFilesHandler.class);

	@Autowired
	private DemFileParser service;

	@Override
	public Mono<ResponseEntity<Void>> handle(CmdProcessAllNewFiles request) {
		_LOGGER.trace("Inside CmdProcessAllNewFilesHandler.handle");

		return service.processAllFiles().map(status -> new ResponseEntity<>(status));
	}

}
