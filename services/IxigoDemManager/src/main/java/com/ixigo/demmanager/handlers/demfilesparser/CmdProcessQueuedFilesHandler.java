package com.ixigo.demmanager.handlers.demfilesparser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.ixigo.demmanager.commands.demfilesparser.CmdProcessQueuedFiles;
import com.ixigo.demmanager.services.interfaces.DemFileParser;
import com.ixigo.library.mediators.web.interfaces.WebCommandHandler;

import reactor.core.publisher.Mono;

/**
 * Handler used to trigger the process that will analyse the queued DEM files
 * 
 * @author marco
 *
 */
@Component
public class CmdProcessQueuedFilesHandler implements WebCommandHandler<CmdProcessQueuedFiles, Void> {
	private static final Logger _LOGGER = LoggerFactory.getLogger(CmdProcessQueuedFilesHandler.class);

	@Autowired
	private DemFileParser service;

	@Override
	public Mono<ResponseEntity<Void>> handle(CmdProcessQueuedFiles request) {
		_LOGGER.trace("Inside CmdProcessNewFilesHandler.handle");

		return service.processQueuedFiles().map(status -> new ResponseEntity<>(status));
	}

}
