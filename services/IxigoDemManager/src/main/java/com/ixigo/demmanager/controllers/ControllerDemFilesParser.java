package com.ixigo.demmanager.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ixigo.demmanager.commands.demfilesparser.CmdProcessFailures;
import com.ixigo.demmanager.commands.demfilesparser.CmdProcessQueuedFiles;
import com.ixigo.demmanager.commands.demfilesparser.CmdQueueAndProcessFiles;
import com.ixigo.library.mediators.web.interfaces.WebMediator;

import io.swagger.annotations.ApiOperation;
import reactor.core.publisher.Mono;

/**
 * This controller exposes APIs to process the DEM files
 * 
 * @author marco
 *
 */
@RestController
@RequestMapping("/parse")
public class ControllerDemFilesParser {
	private static final Logger _LOGGER = LoggerFactory.getLogger(ControllerDemFilesParser.class);

	@Autowired
	private WebMediator mediator;

	@PostMapping("/queued")
	@ApiOperation(value = "It will trigger the scan process for the already queued .dem files")
	public Mono<ResponseEntity<Void>> processQueuedFiles() {
		_LOGGER.trace("Inside ControllerDemFilesParser.processNewFiles");
		return mediator.send(new CmdProcessQueuedFiles());
	}

	@PostMapping("/all")
	@ApiOperation(value = "It will queue new dem files and process the queue")
	public Mono<ResponseEntity<Void>> processAllNewFiles() {
		_LOGGER.trace("Inside ControllerDemFilesParser.processAllNewFiles");
		return mediator.send(new CmdQueueAndProcessFiles());
	}
	
	@PostMapping("/failed")
	@ApiOperation(value = "It will re-process the failures")
	public Mono<ResponseEntity<Void>> processFailures() {
		_LOGGER.trace("Inside ControllerDemFilesParser.processFailures");
		return mediator.send(new CmdProcessFailures());
	}
}
