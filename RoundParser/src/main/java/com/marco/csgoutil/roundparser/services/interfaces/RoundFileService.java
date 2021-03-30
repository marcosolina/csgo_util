package com.marco.csgoutil.roundparser.services.interfaces;

import java.io.File;
import java.util.List;

import com.marco.utils.MarcoException;

public interface RoundFileService {

	/**
	 * It returns a list of all the available .dem files
	 * 
	 * @return
	 * @throws MarcoException
	 */
	public List<File> retrieveAllDemFiles() throws MarcoException;
}
