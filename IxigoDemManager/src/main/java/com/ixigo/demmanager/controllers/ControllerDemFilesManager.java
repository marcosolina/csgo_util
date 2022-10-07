package com.ixigo.demmanager.controllers;


import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ixigo.demmanager.enums.DemProcessStatus;
import com.ixigo.demmanager.models.entities.Dem_process_queueDto;
import com.ixigo.demmanager.models.rest.RestGetFilesResponse;
import com.ixigo.demmanager.repositories.interfaces.RepoProcessQueue;
import com.ixigo.library.mediators.web.interfaces.WebMediator;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/files")
public class ControllerDemFilesManager {
	private static final Logger _LOGGER = LoggerFactory.getLogger(ControllerDemFilesManager.class);

	@Autowired
	private WebMediator mediator;
	@Autowired
	private RepoProcessQueue repo;

	@PostMapping
	public Mono<ResponseEntity<Void>> handleFileUpload(@RequestParam("file") MultipartFile file) {
		_LOGGER.trace("Inside DemFilesManager.handleFileUpload");
		return mediator.send(null);
	}
	
	@GetMapping("/{filename:.+}")
	public Mono<ResponseEntity<Resource>> getFile(@PathVariable String filename) {
		_LOGGER.trace("Inside DemFilesManager.getFile");
		return mediator.send(null);
	}
	
	@GetMapping()
	public Mono<ResponseEntity<RestGetFilesResponse>> getListFiles() {
		_LOGGER.trace("Inside DemFilesManager.getListFiles");
		return mediator.send(null);
	}
	
	@GetMapping("/test")
	public Mono<ResponseEntity<String>> getListTest() {
		_LOGGER.trace("Inside DemFilesManager.getListFiles");
		Dem_process_queueDto dto = new Dem_process_queueDto();
		dto.setFile_name("test");
		dto.setProcess_status(DemProcessStatus.NOT_PROCESSED);
		dto.setProcessed_on(LocalDateTime.now());
		dto.setQueued_on(LocalDateTime.now());
		
		return repo.saveEntity(dto).map(o -> {
			return new ResponseEntity<>(HttpStatus.CREATED);
		});
	}
}
