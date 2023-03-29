package com.ixigo.demmanager.handlers.demdata;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.ixigo.demmanager.commands.demdata.CmdGetUsers;
import com.ixigo.demmanager.mappers.RestMapper;
import com.ixigo.demmanager.services.interfaces.DemFileParser;
import com.ixigo.demmanagercontract.models.rest.demdata.RestUsers;
import com.ixigo.library.mediators.web.interfaces.WebCommandHandler;

import reactor.core.publisher.Mono;

@Component
public class GetUsersHandler implements WebCommandHandler<CmdGetUsers, RestUsers> {
	private static final Logger _LOGGER = LoggerFactory.getLogger(GetUsersHandler.class);
	@Autowired
	private DemFileParser service;
	@Autowired
	private RestMapper mapper;

	@Override
	public Mono<ResponseEntity<RestUsers>> handle(CmdGetUsers request) {
		_LOGGER.trace("Inside GetUsersHandler.handle");
		
		return service.getListOfUsers().collectList().map(list -> {
			RestUsers resp = new RestUsers();
			resp.setUsers(mapper.fromSvcToRestUserList(list));
			return new ResponseEntity<>(resp, HttpStatus.OK);
		});
	}
}
