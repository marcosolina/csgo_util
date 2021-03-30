package com.marco.csgoutil.roundparser.controllers;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.marco.csgoutil.roundparser.model.rest.SingleMapUserScores;
import com.marco.csgoutil.roundparser.services.interfaces.RoundsService;
import com.marco.utils.MarcoException;

@Controller
public class MainController {

	private static final Logger _LOGGER = LoggerFactory.getLogger(MainController.class);
	
	@Autowired
	private RoundsService service;
	
	@GetMapping("")
	public ResponseEntity<SingleMapUserScores> getPlayersAvarageScores() {
		_LOGGER.trace("Inside MainController.getPlayersAvarageScores");
		
		SingleMapUserScores resp = new SingleMapUserScores();
		try {
			Map<String, Double> data = service.processAllDemFiles();
			Map<String, BigDecimal> roundedData = new HashMap<>();

			data.forEach((k, v) -> {
				if(Double.isNaN(v)) {
					v = Double.valueOf(0);
				}
				roundedData.put(k, BigDecimal.valueOf(v).setScale(2, RoundingMode.DOWN));
			});
			
			resp.setUsersScore(roundedData);
			resp.setStatus(true);
		} catch (MarcoException e) {
			resp.addError(e);
		}
		return new ResponseEntity<>(resp, HttpStatus.OK);
	}
}
