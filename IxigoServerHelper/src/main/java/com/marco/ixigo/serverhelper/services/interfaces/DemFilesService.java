package com.marco.ixigo.serverhelper.services.interfaces;

import com.marco.utils.MarcoException;

/**
 * Interface to manage the dem files
 * 
 * @author Marco
 *
 */
public interface DemFilesService {

    /**
     * It copies the new DEM files to the Dem Parser server
     * 
     * @throws MarcoException
     */
    public void sendLastDemFiles(boolean isShutDown) throws MarcoException;
}
