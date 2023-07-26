package com.ixigo.playersmanager.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.ixigo.library.mediators.web.interfaces.WebCommandHandler;
import com.ixigo.playersmanager.commands.CmdGetScoreType;
import com.ixigo.playersmanager.services.interfaces.PlayersManager;
import com.ixigo.playersmanagercontract.models.rest.RestScoreTypes;

import reactor.core.publisher.Mono;

/**
 * Handler use to retrieve the list of available score types in the DEM files
 * 
 * @author marco
 *
 */
@Component
public class GetScoreTypeHandler implements WebCommandHandler<CmdGetScoreType, RestScoreTypes> {
	private static final Logger _LOGGER = LoggerFactory.getLogger(GetScoreTypeHandler.class);
	@Autowired
	private PlayersManager service;

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
