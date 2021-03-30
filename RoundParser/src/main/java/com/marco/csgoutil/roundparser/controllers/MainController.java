package com.marco.csgoutil.roundparser.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.marco.csgoutil.roundparser.services.interfaces.RoundsService;

@Controller
public class MainController {

	@Autowired
	private RoundsService service;
	
	@GetMapping("")
	public ResponseEntity<String> getPlayersNames() {
		service.processAllRounds();
		return new ResponseEntity<>("ciao", HttpStatus.OK);
	}
}
