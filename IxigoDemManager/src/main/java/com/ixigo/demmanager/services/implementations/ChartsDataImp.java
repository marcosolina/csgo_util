package com.ixigo.demmanager.services.implementations;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ixigo.demmanager.enums.ScoreType;
import com.ixigo.demmanager.models.database.DtoMapPlayedCounter;
import com.ixigo.demmanager.models.database.DtoUserAvgScorePerMap;
import com.ixigo.demmanager.models.svc.charts.SvcMapPlayedCounter;
import com.ixigo.demmanager.models.svc.charts.SvcUserAvgScorePerMap;
import com.ixigo.demmanager.repositories.interfaces.RepoUserScore;
import com.ixigo.demmanager.services.interfaces.ChartsData;

import reactor.core.publisher.Flux;
/**
 * My implementation of the @ChartsData
 * @author marco
 *
 */
public class ChartsDataImp implements ChartsData{
	private static final Logger _LOGGER = LoggerFactory.getLogger(ChartsDataImp.class);
	
	@Autowired
	private RepoUserScore repoUserScore;
	
	@Override
	public Flux<SvcMapPlayedCounter> countGamesOnAMap() {
		_LOGGER.trace("Inside: ChartsDataImp.countGamesOnAMap");
		Flux<DtoMapPlayedCounter> maps = repoUserScore.getMapsPlayed();
		return maps.map(dto -> {
			SvcMapPlayedCounter mp = new SvcMapPlayedCounter();
			mp.setCount(dto.getCount());
			mp.setMapName(dto.getMapName());
			return mp;
		});
	}

	@Override
	public Flux<SvcUserAvgScorePerMap> getUserAverageScorePerMap(String steamId, ScoreType scoreType, Optional<List<String>> maps, Optional<Integer> lastMatchesToConsider) {
		_LOGGER.trace("Inside: ChartsDataImp.getUserAverageScorePerMap");
		Flux<DtoUserAvgScorePerMap> scores = repoUserScore.getUserAveragaScorePerMap(steamId, scoreType, maps, lastMatchesToConsider);
		return scores.map(dto -> {
			var svc = new SvcUserAvgScorePerMap();
			svc.setAvgScore(BigDecimal.valueOf(dto.getAvgScore()).setScale(3, RoundingMode.DOWN));
			svc.setMapName(dto.getMapName());
			svc.setSteamId(dto.getSteamId());
			return svc;
		});
	}
}
