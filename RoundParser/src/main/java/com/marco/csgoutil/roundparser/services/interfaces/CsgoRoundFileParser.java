package com.marco.csgoutil.roundparser.services.interfaces;

import java.io.File;
import java.util.List;

import com.marco.csgoutil.roundparser.model.service.UserMapStats;
import com.marco.utils.MarcoException;

/**
 * This provides the functionalities that you can use to parse a dem file
 * 
 * @author Marco
 *
 */
public interface CsgoRoundFileParser {

	/**
	 * It extracts the list of active players and their related scores of the
	 * specific game
	 * 
	 * @param roundFile
	 * @return
	 * @throws MarcoException
	 */
	public List<UserMapStats> extractPlayersScore(File roundFile) throws MarcoException;

}
