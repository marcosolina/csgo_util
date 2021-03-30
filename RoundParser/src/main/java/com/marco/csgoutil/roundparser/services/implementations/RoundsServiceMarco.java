package com.marco.csgoutil.roundparser.services.implementations;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;

import com.marco.csgoutil.roundparser.services.interfaces.CsgoRoundFileParser;
import com.marco.csgoutil.roundparser.services.interfaces.RoundFileService;
import com.marco.csgoutil.roundparser.services.interfaces.RoundsService;

public class RoundsServiceMarco implements RoundsService {

	@Autowired
	private RoundFileService roundFildeService;
	@Autowired
	private CsgoRoundFileParser roundParserService;

	@Override
	public void processAllRounds() {
		File f = roundFildeService.getLatestRoundFile();
		roundParserService.extractPlayersNames(f);
	}

}
