package com.ixigo.serverhelper.services.interfaces;

import com.ixigo.library.errors.IxigoException;

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
    public void postLastDemFiles(boolean isShutDown) throws IxigoException;
}
