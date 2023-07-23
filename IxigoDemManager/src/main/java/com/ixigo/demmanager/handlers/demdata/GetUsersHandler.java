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
import com.ixigo.demmanagercontract.models.rest.demdata.responses.RestUsersResp;
import com.ixigo.library.mediators.web.interfaces.WebCommandHandler;

import reactor.core.publisher.Mono;

/**
 * Handler used to retrieve the list of users seen in the DEM files
 * 
 * @author marco
 *
 */
@Component
public class GetUsersHandler implements WebCommandHandler<CmdGetUsers, RestUsersResp> {
	private static final Logger _LOGGER = LoggerFactory.getLogger(GetUsersHandler.class);
	@Autowired
	private DemFileParser service;
	@Autowired
	private RestMapper mapper;

	@Override
	public Mono<ResponseEntity<RestUsersResp>> handle(CmdGetUsers request) {
		_LOGGER.trace("Inside GetUsersHandler.handle");

		// @formatter:off
		return service.getListOfUsers()
				.map(mapper::fromSvcToRest)
				.collectList()
				.map(list -> {
					RestUsersResp resp = new RestUsersResp();
					resp.setUsers(list);
					return new ResponseEntity<>(resp, HttpStatus.OK);
				});
		// @formatter:on
	}
}
