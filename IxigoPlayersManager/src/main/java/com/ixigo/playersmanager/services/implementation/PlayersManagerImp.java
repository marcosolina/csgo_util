package com.ixigo.playersmanager.services.implementation;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.function.ToLongFunction;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import com.ixigo.demmanagercontract.models.rest.demdata.UsersScoresQueryParam;
import com.ixigo.demmanagercontract.models.rest.demdata.RestMapStats;
import com.ixigo.demmanagercontract.models.rest.demdata.RestUser;
import com.ixigo.demmanagercontract.models.rest.demdata.RestUsers;
import com.ixigo.demmanagercontract.models.rest.demdata.RestUsersScores;
import com.ixigo.library.errors.IxigoException;
import com.ixigo.library.messages.IxigoMessageResource;
import com.ixigo.library.rest.interfaces.IxigoWebClientUtils;
import com.ixigo.playersmanager.RestMapper;
import com.ixigo.playersmanager.config.properties.DemManagersEndPoints;
import com.ixigo.playersmanager.constants.ErrorCodes;
import com.ixigo.playersmanager.models.svc.SvcMapStats;
import com.ixigo.playersmanager.models.svc.SvcTeam;
import com.ixigo.playersmanager.models.svc.SvcUserAvgScore;
import com.ixigo.playersmanager.models.svc.SvcUserMapStats;
import com.ixigo.playersmanager.services.interfaces.PartitionTeams;
import com.ixigo.playersmanager.services.interfaces.PlayersManager;
import com.ixigo.playersmanagercontract.enums.ScoreType;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class PlayersManagerImp implements PlayersManager {
	private static final Logger _LOGGER = LoggerFactory.getLogger(PlayersManagerImp.class);
	@Autowired
	private IxigoMessageResource msgSource;
    @Autowired
    private PartitionTeams ixigoPartition;
    @Autowired
	private IxigoWebClientUtils webClient;
    @Autowired
    private DemManagersEndPoints demManagerEndPoints;
    @Autowired
    private RestMapper mapper;
	
	@Override
	public Flux<SvcTeam> generateTwoTeamsForcingSimilarTeamSizes(Integer numberOfMatches, List<String> usersIDs, double penaltyWeigth, ScoreType scoreType, BigDecimal minPercPlayed) throws IxigoException {
		Flux<SvcUserAvgScore> usersList = getUsersAvg(numberOfMatches, usersIDs, scoreType, minPercPlayed);
		return ixigoPartition.partitionTheUsersIntoTwoTeams(usersList, penaltyWeigth);
	}

	@Override
	public Mono<Map<String, SvcUserAvgScore>> getUsersAvgStatsForLastXGames(Integer numberOfMatches, List<String> usersIDs, ScoreType partionByScore, BigDecimal minPercPlayed) throws IxigoException {
		
		var monoScores = this.getUserScoresFromDemManagerService(numberOfMatches, usersIDs, minPercPlayed);
		var monoKnowUsers = this.getKnownUsersList();
		// @formatter:off
		return Mono.zip(monoScores, monoKnowUsers).map(tuple -> {
			Map<String, List<SvcMapStats>> usersScores = tuple.getT1()
					.entrySet()
					.stream()
					.collect(Collectors.toMap(Map.Entry::getKey, entry -> mapper.fromRestToSvcList(entry.getValue())));
			Map<String, String> userDefinitions = new HashMap<>();
			
			tuple.getT2().forEach(user -> userDefinitions.put(user.getSteamId(), user.getUserName()));
			
			Map<String, SvcUserAvgScore> map = new HashMap<>();
			
			usersScores.forEach((k, v) -> {
				SvcUserAvgScore uas = new SvcUserAvgScore();
	            uas.setSteamID(k);
	            uas.setUserName(userDefinitions.get(k));
	            
	            if (!v.isEmpty()) {
	                uas.setRoundWinShare(           fromBigDecimalToBidecimalAvg(SvcUserMapStats::getRoundWinShare,             v, 2));
	                uas.setKillDeathRatio(          fromBigDecimalToBidecimalAvg(SvcUserMapStats::getKillDeathRation,           v, 2));
	                uas.setHeadShotsPercentage(     fromBigDecimalToBidecimalAvg(SvcUserMapStats::getHeadShotsPercentage,       v, 2));
	                uas.setHalfLifeTelevisionRating(fromBigDecimalToBidecimalAvg(SvcUserMapStats::getHalfLifeTelevisionRating,  v, 3));
	                uas.setKillPerRound(            fromBigDecimalToBidecimalAvg(SvcUserMapStats::getKillPerRound,              v, 2));
	                uas.setAssistsPerRound(         fromBigDecimalToBidecimalAvg(SvcUserMapStats::getAssistsPerRound,           v, 2));
	                uas.setDeathPerRound(           fromBigDecimalToBidecimalAvg(SvcUserMapStats::getDeathPerRound,             v, 2));
	                uas.setAverageDamagePerRound(   fromBigDecimalToBidecimalAvg(SvcUserMapStats::getAverageDamagePerRound,     v, 2));
	                uas.setMatchPlayed(             fromBigDecimalToBidecimalAvg(SvcUserMapStats::getMatchPlayed,               v, 2));
	                
	                uas.setKills(               fromLongToBidecimalAvg(SvcUserMapStats::getKills,                   v, 2));
	                uas.setAssists(             fromLongToBidecimalAvg(SvcUserMapStats::getAssists,                 v, 2));
	                uas.setDeaths(              fromLongToBidecimalAvg(SvcUserMapStats::getDeaths,                  v, 2));
	                uas.setHeadShots(           fromLongToBidecimalAvg(SvcUserMapStats::getHeadShots,               v, 2));
	                uas.setTeamKillFriendlyFire(fromLongToBidecimalAvg(SvcUserMapStats::getTeamKillFriendlyFire,    v, 2));
	                uas.setEntryKill(           fromLongToBidecimalAvg(SvcUserMapStats::getEntryKill,               v, 2));
	                uas.setBombPLanted(         fromLongToBidecimalAvg(SvcUserMapStats::getBombPLanted,             v, 2));
	                uas.setBombDefused(         fromLongToBidecimalAvg(SvcUserMapStats::getBombDefused,             v, 2));
	                uas.setMostValuablePlayer(  fromLongToBidecimalAvg(SvcUserMapStats::getMostValuablePlayer,      v, 2));
	                uas.setScore(               fromLongToBidecimalAvg(SvcUserMapStats::getScore,                   v, 2));
	                uas.setFiveKills(           fromLongToBidecimalAvg(SvcUserMapStats::getFiveKills,               v, 2));
	                uas.setFourKills(           fromLongToBidecimalAvg(SvcUserMapStats::getFourKills,               v, 2));
	                uas.setThreeKills(          fromLongToBidecimalAvg(SvcUserMapStats::getThreeKills,              v, 2));
	                uas.setTwoKills(            fromLongToBidecimalAvg(SvcUserMapStats::getTwoKills,                v, 2));
	                uas.setOneKill(             fromLongToBidecimalAvg(SvcUserMapStats::getOneKill,                 v, 2));
	                uas.setTradeKill(           fromLongToBidecimalAvg(SvcUserMapStats::getTradeKill,               v, 2));
	                uas.setTradeDeath(          fromLongToBidecimalAvg(SvcUserMapStats::getTradeDeath,              v, 2));
	                uas.setTotalDamageHealth(   fromLongToBidecimalAvg(SvcUserMapStats::getTotalDamageHealth,       v, 2));
	                uas.setTotalDamageArmor(    fromLongToBidecimalAvg(SvcUserMapStats::getTotalDamageArmor,        v, 2));
	                uas.setOneVersusOne(        fromLongToBidecimalAvg(SvcUserMapStats::getOneVersusOne,            v, 2));
	                uas.setOneVersusTwo(        fromLongToBidecimalAvg(SvcUserMapStats::getOneVersusTwo,            v, 2));
	                uas.setOneVersusThree(      fromLongToBidecimalAvg(SvcUserMapStats::getOneVersusThree,          v, 2));
	                uas.setOneVersusFour(       fromLongToBidecimalAvg(SvcUserMapStats::getOneVersusFour,           v, 2));
	                uas.setOneVersusFive(       fromLongToBidecimalAvg(SvcUserMapStats::getOneVersusFive,           v, 2));
	                uas.setGrenadesThrownCount( fromLongToBidecimalAvg(SvcUserMapStats::getGrenadesThrownCount,     v, 2));
	                uas.setFlashesThrownCount(  fromLongToBidecimalAvg(SvcUserMapStats::getFlashesThrownCount,      v, 2));
	                uas.setSmokesThrownCount(   fromLongToBidecimalAvg(SvcUserMapStats::getSmokesThrownCount,       v, 2));
	                uas.setFireThrownCount(     fromLongToBidecimalAvg(SvcUserMapStats::getFireThrownCount,         v, 2));
	                uas.setHighExplosiveDamage( fromLongToBidecimalAvg(SvcUserMapStats::getHighExplosiveDamage,     v, 2));
	                uas.setFireDamage(          fromLongToBidecimalAvg(SvcUserMapStats::getFireDamage,              v, 2));
	                
	                
	                Function<SvcUserAvgScore, BigDecimal> function = null;
	                switch (partionByScore) {
	                case RWS:
	                    function = SvcUserAvgScore::getRoundWinShare;
	                    break;
	                case KILLS:
	                    function = SvcUserAvgScore::getKills;
	                    break;
	                case ASSISTS:
	                    function = SvcUserAvgScore::getAssists;
	                    break;
	                case DEATHS:
	                    function = SvcUserAvgScore::getDeaths;
	                    break;
	                case KDR:
	                    function = SvcUserAvgScore::getKillDeathRatio;
	                    break;
	                case HS:
	                    function = SvcUserAvgScore::getHeadShots;
	                    break;
	                case HSP:
	                    function = SvcUserAvgScore::getHeadShotsPercentage;
	                    break;
	                case FF:
	                    function = SvcUserAvgScore::getTeamKillFriendlyFire;
	                    break;
	                case EK:
	                    function = SvcUserAvgScore::getEntryKill;
	                    break;
	                case BP:
	                    function = SvcUserAvgScore::getBombPLanted;
	                    break;
	                case BD:
	                    function = SvcUserAvgScore::getBombDefused;
	                    break;
	                case MVP:
	                    function = SvcUserAvgScore::getMostValuablePlayer;
	                    break;
	                case SCORE:
	                    function = SvcUserAvgScore::getScore;
	                    break;
	                case HLTV:
	                    function = SvcUserAvgScore::getHalfLifeTelevisionRating;
	                    break;
	                case _5K:
	                    function = SvcUserAvgScore::getFiveKills;
	                    break;
	                case _4K:
	                    function = SvcUserAvgScore::getFourKills;
	                    break;
	                case _3K:
	                    function = SvcUserAvgScore::getThreeKills;
	                    break;
	                case _2K:
	                    function = SvcUserAvgScore::getTwoKills;
	                    break;
	                case _1K:
	                    function = SvcUserAvgScore::getOneKill;
	                    break;
	                case TK:
	                    function = SvcUserAvgScore::getTradeKill;
	                    break;
	                case TD:
	                    function = SvcUserAvgScore::getTradeDeath;
	                    break;
	                case KPR:
	                    function = SvcUserAvgScore::getKillPerRound;
	                    break;
	                case APR:
	                    function = SvcUserAvgScore::getAssistsPerRound;
	                    break;
	                case DPR:
	                    function = SvcUserAvgScore::getDeathPerRound;
	                    break;
	                case ADR:
	                    function = SvcUserAvgScore::getAssistsPerRound;
	                    break;
	                case TDH:
	                    function = SvcUserAvgScore::getTotalDamageHealth;
	                    break;
	                case TDA:
	                    function = SvcUserAvgScore::getTotalDamageArmor;
	                    break;
	                case _1V1:
	                    function = SvcUserAvgScore::getOneVersusOne;
	                    break;
	                case _1V2:
	                    function = SvcUserAvgScore::getOneVersusTwo;
	                    break;
	                case _1V3:
	                    function = SvcUserAvgScore::getOneVersusThree;
	                    break;
	                case _1V4:
	                    function = SvcUserAvgScore::getOneVersusFour;
	                    break;
	                case _1V5:
	                    function = SvcUserAvgScore::getOneVersusFive;
	                    break;
	                case GRENADES:
	                    function = SvcUserAvgScore::getGrenadesThrownCount;
	                    break;
	                case FLASHES:
	                    function = SvcUserAvgScore::getFlashesThrownCount;
	                    break;
	                case SMOKES:
	                    function = SvcUserAvgScore::getSmokesThrownCount;
	                    break;
	                case FIRE:
	                    function = SvcUserAvgScore::getFireThrownCount;
	                    break;
	                case HED:
	                    function = SvcUserAvgScore::getHighExplosiveDamage;
	                    break;
	                case FD:
	                    function = SvcUserAvgScore::getFireDamage;
	                    break;
	                }
	                /*
	                 * This is the value that will be used to split the teams.
	                 * I add ZERO so I get a new object
	                 */
	                uas.setTeamSplitScore(function.apply(uas).add(BigDecimal.ZERO));
	                uas.setOriginalTeamSplitScore(function.apply(uas).add(BigDecimal.ZERO));
	            }
	            map.put(k, uas);
			});
			
			return map;
		});
		// @formatter:on
		
	}
	
	
	
	private Mono<Map<String, List<RestMapStats>>> getUserScoresFromDemManagerService(Integer numberOfMatches, List<String> usersIDs, BigDecimal minPercPlayed) throws IxigoException {
		try {
			URL url = new URL(demManagerEndPoints.getGetDemDataUsersScores());
			_LOGGER.debug(url.toString());
			Map<String, String> queryParams = new HashMap<>();
			queryParams.put(UsersScoresQueryParam.NUMBER_OF_MATCHES.getQueryParamKey(), numberOfMatches.toString());
			queryParams.put(UsersScoresQueryParam.USERS_STEAM_IDS.getQueryParamKey(), String.join(",", usersIDs));
	        queryParams.put(UsersScoresQueryParam.MINIMUM_PERCE_MATCH_PLAYED.getQueryParamKey(), minPercPlayed.toString());
			return webClient.performGetRequest(RestUsersScores.class, url, Optional.empty(), Optional.of(queryParams))
					.map(resp -> resp.getBody().getUsersScores());
		}
		catch (MalformedURLException e) {
			e.printStackTrace();
			_LOGGER.error(e.getMessage());
			return Mono.error(new IxigoException(HttpStatus.CONFLICT, msgSource.getMessage(ErrorCodes.GENERIC), ErrorCodes.GENERIC));
		}
	}
	
	private Mono<List<RestUser>> getKnownUsersList() {
		try {
			URL url = new URL(demManagerEndPoints.getGetDemDataUsers());
			_LOGGER.debug(url.toString());
			 return webClient.performGetRequest(RestUsers.class, url, Optional.empty(), Optional.empty()).map(resp -> resp.getBody().getUsers());
		}
		catch (MalformedURLException e) {
			e.printStackTrace();
			_LOGGER.error(e.getMessage());
			 return Mono.error(new IxigoException(HttpStatus.INTERNAL_SERVER_ERROR, msgSource.getMessage(ErrorCodes.GENERIC), ErrorCodes.GENERIC));
		}
	}

	private BigDecimal fromBigDecimalToBidecimalAvg(ToDoubleFunction<SvcUserMapStats> function, List<SvcMapStats> userRecords, int scale) {
        // @formatter:off
        return BigDecimal.valueOf(
                    userRecords.stream()
                    .flatMap(ump -> ump.getUsersStats().stream())
                    .mapToDouble(function::applyAsDouble)
                    .average()
                    .orElse(0)
                ).setScale(scale, RoundingMode.DOWN);
        // @formatter:on
    }
	
	private BigDecimal fromLongToBidecimalAvg(ToLongFunction<SvcUserMapStats> function, List<SvcMapStats> userRecords, int scale) {
        // @formatter:off
        return BigDecimal.valueOf(
                    userRecords.stream()
                    .flatMap(ump -> ump.getUsersStats().stream())
                    .mapToDouble(function::applyAsLong)
                    .average()
                    .orElse(0)
                ).setScale(scale, RoundingMode.DOWN);
        // @formatter:on
    }
	
	private Flux<SvcUserAvgScore> getUsersAvg(Integer numberOfMatches, List<String> usersIDs, ScoreType scoreType, BigDecimal minPercPlayed) throws IxigoException {
		Mono<Map<String, SvcUserAvgScore>> usersAvg = this.getUsersAvgStatsForLastXGames(numberOfMatches, usersIDs, scoreType, minPercPlayed);
		return usersAvg.map(map -> map.values()).flatMapMany(Flux::fromIterable);
    }
}
