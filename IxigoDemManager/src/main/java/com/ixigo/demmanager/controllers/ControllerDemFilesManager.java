package com.ixigo.demmanager.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import com.ixigo.demmanager.commands.demfilesmanager.CmdGetDemFile;
import com.ixigo.demmanager.commands.demfilesmanager.CmdGetDemFilesList;
import com.ixigo.demmanager.commands.demfilesmanager.CmdStoreDemFile;
import com.ixigo.demmanagercontract.models.rest.demfilesmanager.RestGetFilesResponse;
import com.ixigo.library.mediators.web.interfaces.WebMediator;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/files")
public class ControllerDemFilesManager {
	private static final Logger _LOGGER = LoggerFactory.getLogger(ControllerDemFilesManager.class);

	@Autowired
	private WebMediator mediator;

	@PostMapping
	public Mono<ResponseEntity<Void>> handleFileUpload(@RequestParam("file") MultipartFile file) {
		_LOGGER.trace("Inside DemFilesManager.handleFileUpload");
		return mediator.send(new CmdStoreDemFile(file));
	}

	@GetMapping("/{filename:.+}")
	public Mono<ResponseEntity<Resource>> getFile(@PathVariable String filename) {
		_LOGGER.trace("Inside DemFilesManager.getFile");
		return mediator.send(new CmdGetDemFile(filename));
	}

	@GetMapping()
	public Mono<ResponseEntity<RestGetFilesResponse>> getListFiles() {
		_LOGGER.trace("Inside DemFilesManager.getListFiles");
		String url = MvcUriComponentsBuilder.fromMethodName(ControllerDemFilesManager.class, "getListFiles").build().toString();
		return mediator.send(new CmdGetDemFilesList()).map(resp -> {
			resp.getBody().getFiles().forEach((k, v) -> {
				v.parallelStream().forEach(fileInfo -> {
					if (url.endsWith("/")) {
						fileInfo.setUrl(url + fileInfo.getName());
					} else {
						fileInfo.setUrl(url + "/" + fileInfo.getName());
					}
				});
			});
			return resp;
		});
	}
}
