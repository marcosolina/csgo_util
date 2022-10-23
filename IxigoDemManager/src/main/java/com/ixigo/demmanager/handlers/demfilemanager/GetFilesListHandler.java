package com.ixigo.demmanager.handlers.demfilemanager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.ixigo.demmanager.commands.demfilesmanager.CmdGetDemFilesList;
import com.ixigo.demmanager.mappers.RestMapper;
import com.ixigo.demmanager.models.rest.demfilesmanager.RestGetFilesResponse;
import com.ixigo.demmanager.services.interfaces.DemFileManager;
import com.ixigo.library.mediators.web.interfaces.WebCommandHandler;

import reactor.core.publisher.Mono;

@Component
public class GetFilesListHandler implements WebCommandHandler<CmdGetDemFilesList, RestGetFilesResponse> {
	@Autowired
	private DemFileManager service;
	@Autowired
	private RestMapper mapper;

	@Override
	public Mono<ResponseEntity<RestGetFilesResponse>> handle(CmdGetDemFilesList request) {
		return service.loadAllFileNames().map(map -> {
			RestGetFilesResponse resp = new RestGetFilesResponse();
			resp.setFiles(mapper.fromSvcToRest(map));
			return new ResponseEntity<>(resp, HttpStatus.OK);
		});
	}

}
