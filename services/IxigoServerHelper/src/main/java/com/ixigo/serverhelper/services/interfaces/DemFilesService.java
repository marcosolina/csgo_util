package com.ixigo.serverhelper.services.interfaces;

import com.ixigo.library.errors.IxigoException;
import com.ixigo.serverhelper.models.svc.SvcServerMap;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Interface to manage the dem files
 * 
 * @author Marco
 *
 */
public interface DemFilesService {

	/**
	 * It sends the new DEM files to the Dem Manager service
	 * 
	 * @throws MarcoException
	 */
	public Mono<Boolean> postLastDemFiles(boolean isShutDown) throws IxigoException;

	/**
	 * It returns a list of available maps on this server
	 * 
	 * @return
	 * @throws IxigoException
	 */
	public Flux<SvcServerMap> getServerMaps() throws IxigoException;
}
