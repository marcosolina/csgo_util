package com.ixigo.demmanager.handlers.charts;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.ixigo.demmanager.commands.charts.CmdGetMapPlayedCount;
import com.ixigo.demmanager.mappers.RestMapper;
import com.ixigo.demmanager.services.interfaces.ChartsData;
import com.ixigo.demmanagercontract.models.rest.charts.RestMapsPlayed;
import com.ixigo.library.mediators.web.interfaces.WebCommandHandler;

import reactor.core.publisher.Mono;

/**
 * Handler used to manage request to retrieve the number of types the maps where
 * played
 * 
 * @author marco
 *
 */
@Component
public class GetMapPlayedCountHandler implements WebCommandHandler<CmdGetMapPlayedCount, RestMapsPlayed> {
	private static final Logger _LOGGER = LoggerFactory.getLogger(GetMapPlayedCountHandler.class);
	@Autowired
	private ChartsData service;
	@Autowired
	private RestMapper mapper;

	@Override
	public Mono<ResponseEntity<RestMapsPlayed>> handle(CmdGetMapPlayedCount request) {
		_LOGGER.trace("Inside GetMapPlayedCountHandler.handle");

		// @formatter:off
		return service.countGamesOnAMap()
				.collectList()
				.map(mapper::fromSvcToRestMapPlayedList)
				.map(list -> {
					RestMapsPlayed resp = new RestMapsPlayed();
					resp.setMaps(list);
					return new ResponseEntity<>(resp, HttpStatus.OK);
				});
		// @formatter:on
	}

}
