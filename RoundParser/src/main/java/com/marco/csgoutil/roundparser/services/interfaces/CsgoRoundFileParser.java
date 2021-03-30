package com.marco.csgoutil.roundparser.services.interfaces;

import java.io.File;
import java.util.Map;

import com.marco.utils.MarcoException;

public interface CsgoRoundFileParser {

	/**
	 * It parses the .dem file and extract a map of:
	 * 
	 * <ul>
	 * <li>Key -> Player Name</li>
	 * <li>Value -> Score</li>
	 * </ul>
	 * 
	 * @param roundFile
	 * @return
	 */
	public Map<String, Integer> extractPlayersScore(File roundFile) throws MarcoException;
	
}
