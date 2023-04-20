package com.ixigo.demmanager.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ixigo.demmanager.commands.charts.CmdGetMapPlayedCount;
import com.ixigo.demmanagercontract.models.rest.demdata.RestMapsPlayed;
import com.ixigo.library.mediators.web.interfaces.WebMediator;

import io.swagger.annotations.ApiOperation;
import reactor.core.publisher.Mono;

/**
 * It returns the data used to make the charts
 * 
 * @author marco
 *
 */
@RestController
@RequestMapping("/charts")
public class ControllerCharts {
	private static final Logger _LOGGER = LoggerFactory.getLogger(ControllerCharts.class);

	@Autowired
	private WebMediator mediator;

	@GetMapping("/mapsplayed")
	@ApiOperation(value = "It will returns how many times we played a map")
	public Mono<ResponseEntity<RestMapsPlayed>> getMapPlayedCount() {
		_LOGGER.trace("Inside ControllerCharts.getMapPlayedCount");
		return mediator.send(new CmdGetMapPlayedCount());
	}
}
