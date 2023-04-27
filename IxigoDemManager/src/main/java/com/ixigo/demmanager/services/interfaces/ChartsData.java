package com.ixigo.demmanager.services.interfaces;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.ixigo.demmanager.enums.ScoreType;
import com.ixigo.demmanager.models.svc.charts.SvcMapPlayedCounter;
import com.ixigo.demmanager.models.svc.charts.SvcUserAvgScorePerMap;
import com.ixigo.demmanager.models.svc.demdata.SvcMapStats;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ChartsData {
	/**
	 * It returns the number of time we played the different maps
	 * 
	 * @return
	 */
	public Flux<SvcMapPlayedCounter> countGamesOnAMap();

	/**
	 * It returns the average score per map
	 * 
	 * @param steamId
	 * @param scoreType
	 * @return
	 */
	public Flux<SvcUserAvgScorePerMap> getUserAverageScorePerMap(String steamId, ScoreType scoreType, Optional<List<String>> maps, Optional<Integer> lastMatchesToConsider);

	/**
	 * It returns a list of scores for the specific map;
	 * 
	 * @param mapName
	 * @return
	 */
	public Mono<Map<LocalDateTime, SvcMapStats>> getTeamsScorePerMap(String mapName);
}
