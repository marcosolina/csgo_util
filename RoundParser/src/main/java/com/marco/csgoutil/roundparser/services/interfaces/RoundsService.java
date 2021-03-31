package com.marco.csgoutil.roundparser.services.interfaces;

import java.io.File;
import java.util.List;

import com.marco.csgoutil.roundparser.model.rest.User;
import com.marco.csgoutil.roundparser.model.service.MapStats;
import com.marco.utils.MarcoException;

public interface RoundsService {

	/**
	 * It will analyse all the dem files and return the avarage score per player
	 * 
	 * @return
	 * @throws MarcoException
	 */
	public List<MapStats> processAllDemFiles() throws MarcoException;

	public MapStats generateMapStatFromFile(File f) throws MarcoException;

	public List<User> getListOfUsers() throws MarcoException;
	
	public List<MapStats> getUserStats(String steamId) throws MarcoException;
}
