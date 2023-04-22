package com.ixigo.demmanager.services.interfaces;

import com.ixigo.demmanager.enums.ScoreType;
import com.ixigo.demmanager.models.svc.charts.SvcMapPlayedCounter;
import com.ixigo.demmanager.models.svc.charts.SvcUserAvgScorePerMap;

import reactor.core.publisher.Flux;

public interface ChartsData {
	/**
     * It returns the number of time we played the different maps
     * 
     * @return
     */
    public Flux<SvcMapPlayedCounter> countGamesOnAMap();
    
    /**
     * It returns the average score per map
     * @param steamId
     * @param scoreType
     * @return
     */
    public Flux<SvcUserAvgScorePerMap> getUserAverageScorePerMap(String steamId, ScoreType scoreType);
}
