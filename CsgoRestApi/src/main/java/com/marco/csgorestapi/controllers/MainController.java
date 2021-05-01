package com.marco.csgorestapi.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.marco.csgorestapi.model.RconHttpRequest;
import com.marco.csgorestapi.model.RconHttpResponse;
import com.marco.csgorestapi.services.interfaces.RconService;
import com.marco.utils.MarcoException;

import io.swagger.annotations.ApiOperation;

/**
 * Main controller (and probably the only one :) )
 * 
 * @author Marco
 *
 */
@RequestMapping("/cmd")
@RestController
public class MainController {
	private static final Logger LOGGER = LoggerFactory.getLogger(MainController.class);

	@Autowired
	private RconService rconService;

	@PostMapping()
	@ApiOperation(value = "It sends the command to the specific rcon server", code = 200)
	public ResponseEntity<RconHttpResponse> postRconCommand(@RequestBody RconHttpRequest request) {
		LOGGER.trace("Inside MainController.postRconCommand");

		RconHttpResponse resp = new RconHttpResponse();
		try {
			resp.setRconResponse(rconService.sendRconCommand(request.getRconHost(), request.getRconPort(),
					request.getRconPass(), request.getRconCmd()));
			resp.setStatus(true);
		} catch (MarcoException e) {
			resp.addError(e);
		}
		LOGGER.trace("Leaving MainController.postRconCommand");
		return new ResponseEntity<>(resp, HttpStatus.OK);
	}
	
}
