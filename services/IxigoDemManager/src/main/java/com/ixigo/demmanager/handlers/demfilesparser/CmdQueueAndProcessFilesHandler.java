package com.ixigo.demmanager.handlers.demfilesparser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.ixigo.demmanager.commands.demfilesparser.CmdQueueAndProcessFiles;
import com.ixigo.demmanager.services.interfaces.DemFileParser;
import com.ixigo.library.mediators.web.interfaces.WebCommandHandler;

import reactor.core.publisher.Mono;

/**
 * Handler used to trigger the process which will queue the new DEM files and
 * analyse them
 * 
 * @author marco
 *
 */
@Component
public class CmdQueueAndProcessFilesHandler implements WebCommandHandler<CmdQueueAndProcessFiles, Void> {
	private static final Logger _LOGGER = LoggerFactory.getLogger(CmdQueueAndProcessFilesHandler.class);

	@Autowired
	private DemFileParser service;

	@Override
	public Mono<ResponseEntity<Void>> handle(CmdQueueAndProcessFiles request) {
		_LOGGER.trace("Inside CmdProcessAllNewFilesHandler.handle");

		return service.queueAndProcessNewFiles().map(status -> new ResponseEntity<>(status));
	}

}
