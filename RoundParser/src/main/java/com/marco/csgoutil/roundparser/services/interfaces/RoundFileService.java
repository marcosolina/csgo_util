package com.marco.csgoutil.roundparser.services.interfaces;

import java.io.File;
import java.util.List;

import com.marco.utils.MarcoException;

/**
 * It provides the functionalities used to find/retrieve the dem files
 * 
 * @author Marco
 *
 */
public interface RoundFileService {

	/**
	 * It returns a list of all the available .dem files
	 * 
	 * @return
	 * @throws MarcoException
	 */
	public List<File> retrieveAllDemFiles() throws MarcoException;
}
