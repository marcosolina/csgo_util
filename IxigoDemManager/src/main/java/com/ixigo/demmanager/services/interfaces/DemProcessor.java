package com.ixigo.demmanager.services.interfaces;

import java.io.File;

import com.ixigo.demmanager.models.svc.demdata.SvcUserGotvScore;
import com.ixigo.library.errors.IxigoException;

import reactor.core.publisher.Flux;

public interface DemProcessor {
	/**
     * It will extract the info from the dem file and store them in the DB
     * 
     * @param demFile
     * @return
     * @throws MarcoException
     */
    public Flux<SvcUserGotvScore> processDemFile(File demFile) throws IxigoException;
}
