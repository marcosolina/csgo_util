package com.ixigo.playersmanager.services.implementation;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.ToLongFunction;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import com.ixigo.demmanagercontract.models.enums.UsersScoresQueryParam;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestPlayerMatchStatsExtended;
import com.ixigo.demmanagercontract.models.rest.demdata.data.RestUsers;
import com.ixigo.demmanagercontract.models.rest.demdata.responses.RestUsersResp;
import com.ixigo.demmanagercontract.models.rest.demdata.responses.RestUsersScoresResp;
import com.ixigo.library.errors.IxigoException;
import com.ixigo.library.messages.IxigoMessageResource;
import com.ixigo.library.rest.interfaces.IxigoWebClientUtils;
import com.ixigo.playersmanager.config.properties.DemManagersEndPoints;
import com.ixigo.playersmanager.constants.ErrorCodes;
import com.ixigo.playersmanager.mappers.RestMapper;
import com.ixigo.playersmanager.models.svc.SvcMapStats;
import com.ixigo.playersmanager.models.svc.SvcTeam;
import com.ixigo.playersmanager.models.svc.SvcUserAvgScore;
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
	public Flux<SvcTeam> generateTwoTeamsForcingSimilarTeamSizes(Integer numberOfMatches, List<String> usersIDs, double penaltyWeigth, ScoreType scoreType) throws IxigoException {
		Flux<SvcUserAvgScore> usersList = getUsersAvg(numberOfMatches, usersIDs, scoreType);
		return ixigoPartition.partitionTheUsersIntoTwoTeams(usersList, penaltyWeigth);
	}

	@Override
	public Mono<Map<String, SvcUserAvgScore>> getUsersAvgStatsForLastXGames(Integer numberOfMatches, List<String> usersIDs, ScoreType partionByScore) throws IxigoException {

		var monoScores = this.getUserScoresFromDemManagerService(numberOfMatches, usersIDs);
		var monoKnownUsers = this.getKnownUsersList();
		// @formatter:off
		return Mono.zip(monoScores, monoKnownUsers).map(tuple -> {
			Map<String, List<SvcMapStats>> usersScores = tuple.getT1()
				.entrySet()
				.stream()
				.collect(Collectors.toMap(Map.Entry::getKey, entry -> mapper.fromListRestMapStatsToSvcList(entry.getValue())));
				;
			Map<String, String> userDefinitions = new HashMap<>();
			
			tuple.getT2().forEach(user -> userDefinitions.put(user.getSteam_id(), user.getUser_name()));
			
			Map<String, SvcUserAvgScore> map = new HashMap<>();
			
			usersScores.forEach((k, v) -> {
				SvcUserAvgScore uas = new SvcUserAvgScore();
	            uas.setSteamID(k);
	            uas.setUserName(userDefinitions.get(k));
	            
	            if (!v.isEmpty()) {
	            	// TODO use reflection, loop the methods of the class

	            	
	            	
	            	uas.setKills(fromBigDecimalToBidecimalAvg(SvcMapStats::getKills, v, 2));
	            	uas.setTeamKillFriendlyFire(fromBigDecimalToBidecimalAvg(SvcMapStats::getTeamKillFriendlyFire, v, 2));
	            	uas.setHalfLifeTelevisionRating(fromBigDecimalToBidecimalAvg(SvcMapStats::getHalfLifeTelevisionRating, v, 2));
	            	uas.setBombDefused(fromBigDecimalToBidecimalAvg(SvcMapStats::getBombDefused, v, 2));
	            	uas.setFireDamage(fromBigDecimalToBidecimalAvg(SvcMapStats::getFireDamage, v, 2));
	            	uas.setRoundWinShare(fromBigDecimalToBidecimalAvg(SvcMapStats::getRoundWinShare, v, 2));
	            	uas.setHeadShots(fromBigDecimalToBidecimalAvg(SvcMapStats::getHeadShots, v, 2));
	            	uas.setAssists(fromBigDecimalToBidecimalAvg(SvcMapStats::getAssists, v, 2));
	            	uas.setHeadShotsPercentage(fromBigDecimalToBidecimalAvg(SvcMapStats::getHeadShotsPercentage, v, 2));
	            	uas.setDeaths(fromBigDecimalToBidecimalAvg(SvcMapStats::getDeaths, v, 2));
	            	uas.setEntryKill(fromBigDecimalToBidecimalAvg(SvcMapStats::getEntryKill, v, 2));
	            	uas.setDeathPerRound(fromBigDecimalToBidecimalAvg(SvcMapStats::getDeathPerRound, v, 2));
	            	uas.setKillPerRound(fromBigDecimalToBidecimalAvg(SvcMapStats::getKillPerRound, v, 2));
	            	uas.setAverageDamagePerRound(fromBigDecimalToBidecimalAvg(SvcMapStats::getAverageDamagePerRound, v, 2));
	            	uas.setTradeDeath(fromBigDecimalToBidecimalAvg(SvcMapStats::getTradeDeath, v, 2));
	            	uas.setTotalDamageArmor(fromBigDecimalToBidecimalAvg(SvcMapStats::getTotalDamageArmor, v, 2));
	            	uas.setTradeKill(fromBigDecimalToBidecimalAvg(SvcMapStats::getTradeKill, v, 2));
	            	uas.setKillDeathRatio(fromBigDecimalToBidecimalAvg(SvcMapStats::getKillDeathRatio, v, 2));
	            	uas.setTotalDamageHealth(fromBigDecimalToBidecimalAvg(SvcMapStats::getTotalDamageHealth, v, 2));
	            	uas.setKast(fromBigDecimalToBidecimalAvg(SvcMapStats::getKast, v, 2));
	            	uas.setHostageRescued(fromBigDecimalToBidecimalAvg(SvcMapStats::getHostageRescued, v, 2));
	            	uas.setBombPlanted(fromBigDecimalToBidecimalAvg(SvcMapStats::getBombPlanted, v, 2));
	            	uas.setRoundWinShareTotal(fromBigDecimalToBidecimalAvg(SvcMapStats::getRoundWinShareTotal, v, 2));
	            	uas.setTeammateDamageHealth(fromBigDecimalToBidecimalAvg(SvcMapStats::getTeammateDamageHealth, v, 2));
	            	uas.setOpponentBlindTime(fromBigDecimalToBidecimalAvg(SvcMapStats::getOpponentBlindTime, v, 2));
	            	uas.setTeammateBlindTime(fromBigDecimalToBidecimalAvg(SvcMapStats::getTeammateBlindTime, v, 2));
	            	uas.setFlashAssists(fromBigDecimalToBidecimalAvg(SvcMapStats::getFlashAssists, v, 2));

	                
	            	uas.setOneVersusThree(fromLongToBidecimalAvg(SvcMapStats::getOneVersusThree, v, 2));
	            	uas.setOneVersusTwo(fromLongToBidecimalAvg(SvcMapStats::getOneVersusTwo, v, 2));
	            	uas.setOneVersusOne(fromLongToBidecimalAvg(SvcMapStats::getOneVersusOne, v, 2));
	            	uas.setFourKills(fromLongToBidecimalAvg(SvcMapStats::getFourKills, v, 2));
	            	uas.setTwoKills(fromLongToBidecimalAvg(SvcMapStats::getTwoKills, v, 2));
	            	uas.setOneVersusFive(fromLongToBidecimalAvg(SvcMapStats::getOneVersusFive, v, 2));
	            	uas.setOneVersusFour(fromLongToBidecimalAvg(SvcMapStats::getOneVersusFour, v, 2));
	            	uas.setScore(fromLongToBidecimalAvg(SvcMapStats::getScore, v, 2));
	            	uas.setMostValuablePlayer(fromLongToBidecimalAvg(SvcMapStats::getMostValuablePlayer, v, 2));
	            	uas.setFiveKills(fromLongToBidecimalAvg(SvcMapStats::getFiveKills, v, 2));
	            	uas.setThreeKills(fromLongToBidecimalAvg(SvcMapStats::getThreeKills, v, 2));
	            	uas.setOneKill(fromLongToBidecimalAvg(SvcMapStats::getOneKill, v, 2));
	            	uas.setKasttotal(fromLongToBidecimalAvg(SvcMapStats::getKasttotal, v, 2));
	            	uas.setRoundsOnTeam2(fromLongToBidecimalAvg(SvcMapStats::getRoundsOnTeam2, v, 2));
	            	uas.setRoundsOnTeam1(fromLongToBidecimalAvg(SvcMapStats::getRoundsOnTeam1, v, 2));
	            	uas.setRoundsplayed(fromLongToBidecimalAvg(SvcMapStats::getRoundsplayed, v, 2));

	                
	                //TODO set the values
	                Function<SvcUserAvgScore, BigDecimal> function = null;
	                switch (partionByScore) {
	                case kills:
	                    function = SvcUserAvgScore::getKills;
	                    break;
	                case ffk:
	                	function = SvcUserAvgScore::getTeamKillFriendlyFire;
	                    break;
	                case hltv_rating:
	                	function = SvcUserAvgScore::getHalfLifeTelevisionRating;
	                    break;
	                case bd:
	                	function = SvcUserAvgScore::getBombDefused;
	                    break;
	                case _1v3:
	                	function = SvcUserAvgScore::getOneVersusThree;
	                    break;
	                case _1v2:
	                	function = SvcUserAvgScore::getOneVersusTwo;
	                    break;
	                case kast:
	                	function = SvcUserAvgScore::getKast;
	                    break;
	                case _1v1:
	                	function = SvcUserAvgScore::getOneVersusOne;
	                    break;
	                case hr:
	                	function = SvcUserAvgScore::getHostageRescued;
	                    break;
	                case bp:
	                	function = SvcUserAvgScore::getBombPlanted;
	                    break;
	                case ud:
	                	function = SvcUserAvgScore::getFireDamage;
	                    break;
	                case kast_total:
	                	function = SvcUserAvgScore::getKasttotal;
	                    break;
	                case rws:
	                	function = SvcUserAvgScore::getRoundWinShare;
	                    break;
	                case score:
	                	function = SvcUserAvgScore::getScore;
	                    break;
	                case headshots:
	                	function = SvcUserAvgScore::getHeadShots;
	                    break;
	                case assists:
	                	function = SvcUserAvgScore::getAssists;
	                    break;
	                case _4k:
	                	function = SvcUserAvgScore::getFourKills;
	                    break;
	                case _2k:
	                	function = SvcUserAvgScore::getTwoKills;
	                    break;
	                case _1v5:
	                	function = SvcUserAvgScore::getOneVersusFive;
	                    break;
	                case _1v4:
	                	function = SvcUserAvgScore::getOneVersusFour;
	                    break;
	                case headshot_percentage:
	                	function = SvcUserAvgScore::getHeadShotsPercentage;
	                    break;
	                case deaths:
	                	function = SvcUserAvgScore::getDeaths;
	                    break;
	                case ffd:
	                	function = SvcUserAvgScore::getTeammateDamageHealth;
	                    break;
	                case ek:
	                	function = SvcUserAvgScore::getEntryKill;
	                    break;
	                case mvp:
	                	function = SvcUserAvgScore::getMostValuablePlayer;
	                    break;
	                case dpr:
	                	function = SvcUserAvgScore::getDeathPerRound;
	                    break;
	                case rws_total:
	                	function = SvcUserAvgScore::getRoundWinShareTotal;
	                    break;
	                case kpr:
	                	function = SvcUserAvgScore::getKillPerRound;
	                    break;
	                case adr:
	                	function = SvcUserAvgScore::getAverageDamagePerRound;
	                    break;
	                case td:
	                	function = SvcUserAvgScore::getTradeDeath;
	                    break;
	                case tda:
	                	function = SvcUserAvgScore::getTotalDamageArmor;
	                    break;
	                case _5k:
	                	function = SvcUserAvgScore::getFiveKills;
	                    break;
	                case _3k:
	                	function = SvcUserAvgScore::getThreeKills;
	                    break;
	                case ebt:
	                	function = SvcUserAvgScore::getOpponentBlindTime;
	                    break;
	                case tk:
	                	function = SvcUserAvgScore::getTradeKill;
	                    break;
	                case kdr:
	                	function = SvcUserAvgScore::getKillDeathRatio;
	                    break;
	                case _1k:
	                	function = SvcUserAvgScore::getOneKill;
	                    break;
	                case tdh:
	                	function = SvcUserAvgScore::getTotalDamageHealth;
	                    break;
	                case fbt:
	                	function = SvcUserAvgScore::getTeammateBlindTime;
	                    break;
	                case fa:
	                	function = SvcUserAvgScore::getFlashAssists;
	                    break;
	                default:
	                	throw new IxigoException(HttpStatus.BAD_REQUEST, msgSource.getMessage(ErrorCodes.SCORE_TYPE_NOT_SUPPORTED), ErrorCodes.SCORE_TYPE_NOT_SUPPORTED);
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

	private Mono<Map<String, List<RestPlayerMatchStatsExtended>>> getUserScoresFromDemManagerService(Integer numberOfMatches, List<String> usersIDs) throws IxigoException {
		try {
			URL url = new URL(demManagerEndPoints.getGetDemDataUsersScores());
			Map<String, String> queryParams = new HashMap<>();
			queryParams.put(UsersScoresQueryParam.NUMBER_OF_MATCHES.getQueryParamKey(), numberOfMatches.toString());
			queryParams.put(UsersScoresQueryParam.USERS_STEAM_IDS.getQueryParamKey(), String.join(",", usersIDs));

			_LOGGER.debug(url.toString());
			_LOGGER.debug(String.format("Query paramas: %s", queryParams.toString()));

			return webClient.performGetRequest(RestUsersScoresResp.class, url, Optional.empty(), Optional.of(queryParams)).map(resp -> resp.getBody().getUserScores());
		} catch (MalformedURLException e) {
			e.printStackTrace();
			_LOGGER.error(e.getMessage());
			return Mono.error(new IxigoException(HttpStatus.CONFLICT, msgSource.getMessage(ErrorCodes.GENERIC), ErrorCodes.GENERIC));
		}
	}

	private Mono<List<RestUsers>> getKnownUsersList() {
		try {
			URL url = new URL(demManagerEndPoints.getGetDemDataUsers());
			_LOGGER.debug(url.toString());
			return webClient.performGetRequest(RestUsersResp.class, url, Optional.empty(), Optional.empty()).map(resp -> resp.getBody().getUsers());
		} catch (MalformedURLException e) {
			e.printStackTrace();
			_LOGGER.error(e.getMessage());
			return Mono.error(new IxigoException(HttpStatus.INTERNAL_SERVER_ERROR, msgSource.getMessage(ErrorCodes.GENERIC), ErrorCodes.GENERIC));
		}
	}

	private BigDecimal fromBigDecimalToBidecimalAvg(Function<SvcMapStats, BigDecimal> function, List<SvcMapStats> userRecords, int scale) {
		// @formatter:off
        return BigDecimal.valueOf(
                    userRecords.stream()
                    .map(function)
                    .mapToDouble( z -> z.doubleValue())
                    .average()
                    .orElse(0)
                ).setScale(scale, RoundingMode.DOWN);
        // @formatter:on
	}

	private BigDecimal fromLongToBidecimalAvg(ToLongFunction<SvcMapStats> function, List<SvcMapStats> userRecords, int scale) {
		// @formatter:off
        return BigDecimal.valueOf(
                    userRecords.stream()
                    .mapToDouble(function::applyAsLong)
                    .average()
                    .orElse(0)
                ).setScale(scale, RoundingMode.DOWN);
        // @formatter:on
	}

	private Flux<SvcUserAvgScore> getUsersAvg(Integer numberOfMatches, List<String> usersIDs, ScoreType scoreType) throws IxigoException {
		Mono<Map<String, SvcUserAvgScore>> usersAvg = this.getUsersAvgStatsForLastXGames(numberOfMatches, usersIDs, scoreType);
		return usersAvg.map(map -> map.values()).flatMapMany(Flux::fromIterable);
	}

	@Override
	public Mono<Map<String, String>> mapOfAvailableScores() {
		Map<String, String> map = new HashMap<>();
		Arrays.stream(ScoreType.values()).forEach(s -> map.put(s.name(), s.getDesc()));
		return Mono.just(map);
	}
}
