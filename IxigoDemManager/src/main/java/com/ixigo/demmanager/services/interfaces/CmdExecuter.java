package com.ixigo.demmanager.services.interfaces;

import java.util.List;

import com.ixigo.demmanager.models.svc.demdata.SvcUserGotvScore;
import com.ixigo.library.errors.IxigoException;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Execute the command on the OS to extract the information from the DEM files
 * 
 * @author marco
 *
 */
public interface CmdExecuter {
	/**
	 * It will run the command on the OS to extract the info from the DEM file
	 * 
	 * @param cmd -> The command + parameters for the DEM parser data extractor
	 * @return The list of scores extracted from the DEM file
	 * @throws IxigoException
	 */
	public Flux<SvcUserGotvScore> extractPlayersScore(List<String> cmd) throws IxigoException;

	/**
	 * It will run the command on the OS and return the output
	 * 
	 * @param cmd
	 * @return
	 * @throws IxigoException
	 */
	public Mono<String> runCommand(List<String> cmd) throws IxigoException;
}
