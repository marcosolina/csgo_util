package com.ixigo.demmanager.handlers.demfilemanager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.ixigo.demmanager.commands.demfilesmanager.CmdGetDemFile;
import com.ixigo.demmanager.services.interfaces.DemFileManager;
import com.ixigo.library.mediators.web.interfaces.WebCommandHandler;

import reactor.core.publisher.Mono;

/**
 * Handler used to manage the request to retrieve a DEM file
 * 
 * @author marco
 *
 */
@Component
public class GetFileHandler implements WebCommandHandler<CmdGetDemFile, Resource> {
	@Autowired
	private DemFileManager service;

	@Override
	public Mono<ResponseEntity<Resource>> handle(CmdGetDemFile request) {

		// @formatter:off
		return service.load(request.getFileName())
			.map(resource -> {
			return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
					.body(resource);
			}).defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
		// @formatter:on
	}

}
