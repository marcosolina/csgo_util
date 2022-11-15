package com.ixigo.serverhelper.services.interfaces;

import com.ixigo.library.errors.IxigoException;

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
    public Mono<Void> postLastDemFiles(boolean isShutDown) throws IxigoException;
}
