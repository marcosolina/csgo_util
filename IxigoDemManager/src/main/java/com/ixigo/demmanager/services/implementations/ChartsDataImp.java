package com.ixigo.demmanager.services.implementations;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ixigo.demmanager.enums.ScoreType;
import com.ixigo.demmanager.mappers.SvcMapper;
import com.ixigo.demmanager.models.database.DtoMapPlayedCounter;
import com.ixigo.demmanager.models.database.DtoUserAvgScorePerMap;
import com.ixigo.demmanager.models.database.Users_scoresDto;
import com.ixigo.demmanager.models.svc.charts.SvcMapPlayedCounter;
import com.ixigo.demmanager.models.svc.charts.SvcUserAvgScorePerMap;
import com.ixigo.demmanager.models.svc.demdata.SvcMapStats;
import com.ixigo.demmanager.models.svc.demdata.SvcUser;
import com.ixigo.demmanager.repositories.interfaces.RepoUserScore;
import com.ixigo.demmanager.services.interfaces.ChartsData;
import com.ixigo.demmanager.services.interfaces.DemFileParser;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
/**
 * My implementation of the @ChartsData
 * @author marco
 *
 */
public class ChartsDataImp implements ChartsData{
	private static final Logger _LOGGER = LoggerFactory.getLogger(ChartsDataImp.class);
	@Autowired
	private DemFileParser demService;
	@Autowired
	private RepoUserScore repoUserScore;
	@Autowired
	private SvcMapper mapper;
	
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

	@Override
	public Mono<Map<LocalDateTime, SvcMapStats>> getTeamsScorePerMap(String mapName) {
		var users = demService.getListOfUsers().collectList();
		var scores = repoUserScore.getUserScoresPerMap(mapName).collectList();
		// @formatter:off 
		return Mono.zip(users, scores)
			.map(tuple -> {
				Map<LocalDateTime, SvcMapStats> statsMap = new HashMap<>();
				List<SvcUser> listOfUsers = tuple.getT1();
				List<Users_scoresDto> listOfScores = tuple.getT2();
				for (Users_scoresDto score : listOfScores) {
					statsMap.compute(score.getGame_date(), (k,v) -> {
						SvcUser user = listOfUsers.stream().filter(u -> u.getSteamId().equals(score.getSteam_id())).findFirst().get();
						if (v == null) {
			                v = mapper.fromUsersScoreDtoToSvcMapStata(mapper.fromSvcToDto(user), score);
			            }
			            v.addUserMapStats(mapper.fromUsersScoreDtoToSvcMapStata(mapper.fromSvcToDto(user), score).getUsersStats().get(0)); 
			            return v;
					});
				} 
				
				return statsMap;
			});
		// @formatter:on
	}
}
