package com.ixigo.serverhelper.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.ixigo.library.mediators.web.interfaces.WebCommandHandler;
import com.ixigo.serverhelper.commands.GetMapsCmd;
import com.ixigo.serverhelper.mappers.RestMapper;
import com.ixigo.serverhelper.models.rest.RestServerMaps;
import com.ixigo.serverhelper.services.interfaces.DemFilesService;

import reactor.core.publisher.Mono;

@Component
public class GetMapsCmdHandler implements WebCommandHandler<GetMapsCmd, RestServerMaps> {
	private static final Logger LOGGER = LoggerFactory.getLogger(GetMapsCmdHandler.class);

	@Autowired
	private DemFilesService service;
	@Autowired
	private RestMapper mapper;

	@Override
	public Mono<ResponseEntity<RestServerMaps>> handle(GetMapsCmd cmd) {
		LOGGER.trace("Inside GetMapsCmdHandler.handle");
		// @formatter:off
		return service.getServerMaps()
			.map(mapper::fromSvcToRest)
			.collectList()
			.map(list -> {
				RestServerMaps maps = new RestServerMaps();
				maps.setMaps(list);
				return maps;
			})
			.map(maps -> new ResponseEntity<>(maps, HttpStatus.OK))
			;
		// @formatter:on
	}

}
