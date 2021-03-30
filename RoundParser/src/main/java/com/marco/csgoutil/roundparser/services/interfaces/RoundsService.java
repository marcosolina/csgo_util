package com.marco.csgoutil.roundparser.services.interfaces;

import java.util.Map;

import com.marco.utils.MarcoException;

public interface RoundsService {

	/**
	 * It will analyse all the dem files and return the avarage score per player
	 * 
	 * @return
	 * @throws MarcoException
	 */
	public Map<String, Double> processAllDemFiles() throws MarcoException;
}
