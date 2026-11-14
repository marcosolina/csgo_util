package com.ixigo.demmanager.controllers;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ixigo.demmanager.commands.charts.CmdGetViewData;
import com.ixigo.demmanagercontract.models.rest.charts.RestViewData;
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

	@GetMapping("/view/{view_name}")
	@ApiOperation(value = "It will return how many times we played a map")
	public Mono<ResponseEntity<RestViewData>> getViewData(@PathVariable("view_name") String viewName, @RequestParam Map<String,String> allRequestParams) {
		_LOGGER.trace("Inside ControllerCharts.getMapPlayedCount");
		return mediator.send(new CmdGetViewData(viewName, allRequestParams));
	}
}
