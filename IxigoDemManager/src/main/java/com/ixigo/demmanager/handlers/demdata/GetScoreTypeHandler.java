package com.ixigo.demmanager.handlers.demdata;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.ixigo.demmanager.commands.demdata.CmdGetScoreType;
import com.ixigo.demmanager.models.rest.demdata.RestScoreTypes;
import com.ixigo.demmanager.services.interfaces.DemFileParser;
import com.ixigo.library.mediators.web.interfaces.WebCommandHandler;

import reactor.core.publisher.Mono;

@Component
public class GetScoreTypeHandler implements WebCommandHandler<CmdGetScoreType, RestScoreTypes> {
	private static final Logger _LOGGER = LoggerFactory.getLogger(GetScoreTypeHandler.class);
	@Autowired
	private DemFileParser service;

	@Override
	public Mono<ResponseEntity<RestScoreTypes>> handle(CmdGetScoreType request) {
		_LOGGER.trace("Inside GetScoreTypeHandler.handle");
		
		return service.mapOfAvailableScores().map(m -> {
			RestScoreTypes resp = new RestScoreTypes();
			resp.setTypes(m);
			return new ResponseEntity<>(resp, HttpStatus.OK);
		});
	}

}
