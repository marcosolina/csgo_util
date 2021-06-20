package com.marco.ixigo.demmanager.services.interfaces;

import java.io.File;
import java.util.List;

import com.marco.ixigo.demmanager.model.dto.UserMapStats;
import com.marco.utils.MarcoException;

public interface DemProcessor {

    /**
     * It will extract the info from the dem file and store them in the DB
     * 
     * @param demFile
     * @return
     * @throws MarcoException
     */
    public List<UserMapStats> processDemFile(File demFile) throws MarcoException;
}
