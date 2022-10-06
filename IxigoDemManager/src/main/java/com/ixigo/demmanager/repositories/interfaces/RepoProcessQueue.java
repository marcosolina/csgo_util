package com.ixigo.demmanager.repositories.interfaces;

import com.ixigo.demmanager.models.entities.DemProcessQueueDto;

import reactor.core.publisher.Flux;

public interface RepoProcessQueue {
	/**
     * It returns a list of DEM files that are not processed yet
     * 
     * @return
     */
    public Flux<DemProcessQueueDto> getNotProcessedDemFiles();
}
