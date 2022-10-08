package com.ixigo.demmanager.handlers.demfilemanager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.ixigo.demmanager.commands.CmdStoreDemFile;
import com.ixigo.demmanager.services.interfaces.DemFileManager;
import com.ixigo.library.mediators.web.interfaces.WebCommandHandler;

import reactor.core.publisher.Mono;

@Component
public class StoreDemFileHandler implements WebCommandHandler<CmdStoreDemFile, Void> {

	@Autowired
	private DemFileManager service;

	@Override
	public Mono<ResponseEntity<Void>> handle(CmdStoreDemFile request) {
		return service.store(request.getFile()).thenReturn(new ResponseEntity<Void>(HttpStatus.CREATED));
	}

}
