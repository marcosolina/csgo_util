package com.ixigo.demmanager.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ixigo.demmanager.commands.demfilesparser.CmdProcessAllNewFiles;
import com.ixigo.demmanager.commands.demfilesparser.CmdProcessNonProcessedFiles;
import com.ixigo.library.mediators.web.interfaces.WebMediator;

import io.swagger.annotations.ApiOperation;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/parse")
public class ControllerDemFilesParser {
	private static final Logger _LOGGER = LoggerFactory.getLogger(ControllerDemFilesParser.class);

	@Autowired
	private WebMediator mediator;

	@PostMapping
	@ApiOperation(value = "It will trigger the scan process for new .dem files")
	public Mono<ResponseEntity<Void>> processNonProcessedFiles() {
		_LOGGER.trace("Inside ControllerDemFilesParser.processNewFiles");
		return mediator.send(new CmdProcessNonProcessedFiles());
	}

	@PostMapping("/all")
	@ApiOperation(value = "It will trigger the scan process for all the new .dem files")
	public Mono<ResponseEntity<Void>> processAllNewFiles() {
		_LOGGER.trace("Inside ControllerDemFilesParser.processAllNewFiles");
		return mediator.send(new CmdProcessAllNewFiles());
	}
}
