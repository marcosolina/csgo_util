package com.ixigo.demmanager.services.interfaces;

import java.util.List;

import com.ixigo.library.errors.IxigoException;

import reactor.core.publisher.Mono;

/**
 * Execute the command on the OS to extract the information from the DEM files
 * 
 * @author marco
 *
 */
public interface CmdExecuter {
	/**
	 * It will run the command on the OS and return the output
	 * 
	 * @param cmd
	 * @return
	 * @throws IxigoException
	 */
	public Mono<String> runCommand(List<String> cmd) throws IxigoException;
}
