package com.ixigo.demmanager.handlers.demfilemanager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.ixigo.demmanager.commands.demfilesmanager.CmdRemoveDemFileFromQueue;
import com.ixigo.demmanager.services.interfaces.DemFileManager;
import com.ixigo.library.mediators.web.interfaces.WebCommandHandler;

import reactor.core.publisher.Mono;

/**
 * Handler used to manage the request to delete a DEM file
 * 
 * @author marco
 *
 */
@Component
public class CmdRemoveDemFileFromQueueHandler implements WebCommandHandler<CmdRemoveDemFileFromQueue, Void> {
	@Autowired
	private DemFileManager service;

	@Override
	public Mono<ResponseEntity<Void>> handle(CmdRemoveDemFileFromQueue request) {

		// @formatter:off
		return service.removeFromQueue(request.getFileName())
				.map(bool -> {
					return new ResponseEntity<Void>(bool ? HttpStatus.OK : HttpStatus.NOT_FOUND);
				}).defaultIfEmpty(new ResponseEntity<Void>(HttpStatus.NOT_FOUND));
		// @formatter:on
	}

}
