package com.ixigo.demmanager.handlers.demdata;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.ixigo.demmanager.commands.demdata.CmdGetMapPlayedCount;
import com.ixigo.demmanager.mappers.RestMapper;
import com.ixigo.demmanager.services.interfaces.DemFileParser;
import com.ixigo.demmanagercontract.models.rest.demdata.RestMapsPlayed;
import com.ixigo.library.mediators.web.interfaces.WebCommandHandler;

import reactor.core.publisher.Mono;

@Component
public class GetMapPlayedCountHandler implements WebCommandHandler<CmdGetMapPlayedCount, RestMapsPlayed> {
	private static final Logger _LOGGER = LoggerFactory.getLogger(GetMapPlayedCountHandler.class);
	@Autowired
	private DemFileParser service;
	@Autowired
	private RestMapper mapper;
	
	@Override
	public Mono<ResponseEntity<RestMapsPlayed>> handle(CmdGetMapPlayedCount request) {
		_LOGGER.trace("Inside GetMapPlayedCountHandler.handle");
		
		return service.countGamesOnAMap().collectList().map(l -> {
			RestMapsPlayed resp = new RestMapsPlayed();
			resp.setMaps(mapper.fromSvcToRestMapPlayedList(l));
			return new ResponseEntity<>(resp, HttpStatus.OK);
		});
	}

}
