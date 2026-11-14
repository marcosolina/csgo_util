package com.ixigo.demmanager.services.interfaces;

import java.io.File;

import com.ixigo.demmanager.models.svc.demdata.nodejs.SvcNodeJsParseOutput;
import com.ixigo.library.errors.IxigoException;

import reactor.core.publisher.Mono;

public interface DemProcessor {
	/**
	 * It will extract the info from the dem file and store them in the DB
	 * 
	 * @param demFile
	 * @return
	 * @throws MarcoException
	 */
	public Mono<SvcNodeJsParseOutput> processDemFile(File demFile) throws IxigoException;
}
