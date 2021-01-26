package com.marco.csgorestapi.controllers;

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

@RequestMapping("/cmd")
@RestController
public class MainController {

	@Autowired
	private RconService rconService;

	@PostMapping()
	@ApiOperation(value = "It sends the command to the specific rcon server", code = 200)
	public  ResponseEntity<RconHttpResponse> postRconCommand(@RequestBody RconHttpRequest request) {
		RconHttpResponse resp = new RconHttpResponse();
		try {
			resp.setRconResponse(rconService.sendRconCommand(request.getRconHost(), request.getRconPort(),
					request.getRconPass(), request.getRconCmd()));
			resp.setStatus(true);
		} catch (Exception e) {
			resp.addError(new MarcoException(e));
		}
		return new ResponseEntity<>(resp, HttpStatus.OK);
	}
	
}
