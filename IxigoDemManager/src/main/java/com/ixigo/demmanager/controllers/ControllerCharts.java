package com.ixigo.demmanager.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ixigo.demmanager.commands.charts.CmdGetAvgScorePerMap;
import com.ixigo.demmanager.commands.charts.CmdGetMapPlayedCount;
import com.ixigo.demmanager.commands.charts.CmdGetTeamsScorePerMap;
import com.ixigo.demmanagercontract.models.rest.charts.RestAvgScoresPerMap;
import com.ixigo.demmanagercontract.models.rest.charts.RestMapsPlayed;
import com.ixigo.demmanagercontract.models.rest.charts.RestTeamScorePerMap;
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
	@ApiOperation(value = "It will return how many times we played a map")
	public Mono<ResponseEntity<RestMapsPlayed>> getMapPlayedCount() {
		_LOGGER.trace("Inside ControllerCharts.getMapPlayedCount");
		return mediator.send(new CmdGetMapPlayedCount());
	}
	
	@GetMapping("/avgscore")
	@ApiOperation(value = "It will returnthe average score per map of the players")
	public Mono<ResponseEntity<RestAvgScoresPerMap>> getAverageScorePerMap(
			@RequestParam(name = "steamIds", required = false) List<String> steamIds,
			@RequestParam(name = "scoreType", required = false) String scoreType,
			@RequestParam(name = "maps", required = false) List<String> maps,
			@RequestParam(name = "matchesToConsider", required = false) String matchesToConsider
			){
		_LOGGER.trace("Inside ControllerCharts.getAverageScorePerMap");
		return mediator.send(new CmdGetAvgScorePerMap(steamIds, scoreType, maps, matchesToConsider));
	}
	
	@GetMapping("/scorepermap")
	@ApiOperation(value = "It will returnthe average score per map of the players")
	public Mono<ResponseEntity<RestTeamScorePerMap>> getTeamsScorePerMap(
			@RequestParam(name = "map", required = false) String mapName,
			@RequestParam(name = "matchesToConsider", required = false) String matchesToConsider){
		_LOGGER.trace("Inside ControllerCharts.getTeamsScorePerMap");
		return mediator.send(new CmdGetTeamsScorePerMap(mapName, matchesToConsider));
	}
}
