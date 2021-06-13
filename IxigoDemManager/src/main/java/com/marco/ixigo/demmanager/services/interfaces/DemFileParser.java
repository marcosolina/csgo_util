package com.marco.ixigo.demmanager.services.interfaces;

import com.marco.utils.MarcoException;

public interface DemFileParser {
    /**
     * In checks in the queue if there are new files to be processed
     * 
     * @return
     * @throws MarcoException
     */
    public boolean processFiles() throws MarcoException;
}
