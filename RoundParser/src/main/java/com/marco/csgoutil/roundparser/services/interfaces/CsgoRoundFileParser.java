package com.marco.csgoutil.roundparser.services.interfaces;

import java.io.File;
import java.util.List;

import com.marco.csgoutil.roundparser.model.service.UserMapStats;
import com.marco.utils.MarcoException;

public interface CsgoRoundFileParser {

	public List<UserMapStats> extractPlayersScore(File roundFile) throws MarcoException;
	
}
