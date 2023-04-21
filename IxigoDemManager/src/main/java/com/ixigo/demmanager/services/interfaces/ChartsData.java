package com.ixigo.demmanager.services.interfaces;

import com.ixigo.demmanager.models.svc.demdata.SvcMapPlayedCounter;

import reactor.core.publisher.Flux;

public interface ChartsData {
	/**
     * It returns the number of time we played the different maps
     * 
     * @return
     */
    public Flux<SvcMapPlayedCounter> countGamesOnAMap();
}
