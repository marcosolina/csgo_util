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
	public Flux<SvcTeam> generateTwoTeamsForcingSimilarTeamSizes(Integer numberOfMatches, List<String> usersIDs, double penaltyWeigth, ScoreType scoreType, BigDecimal minPercPlayed) throws IxigoException {
		Flux<SvcUserAvgScore> usersList = getUsersAvg(numberOfMatches, usersIDs, scoreType, minPercPlayed);
		return ixigoPartition.partitionTheUsersIntoTwoTeams(usersList, penaltyWeigth);
	}

	@Override
	public Mono<Map<String, SvcUserAvgScore>> getUsersAvgStatsForLastXGames(Integer numberOfMatches, List<String> usersIDs, ScoreType partionByScore, BigDecimal minPercPlayed) throws IxigoException {
		
		var monoScores = this.getUserScoresFromDemManagerService(numberOfMatches, usersIDs, minPercPlayed);
		var monoKnownUsers = this.getKnownUsersList();
		// @formatter:off
		return Mono.zip(monoScores, monoKnownUsers).map(tuple -> {
			Map<String, List<SvcMapStats>> usersScores = new HashMap<>();
			tuple.getT1()
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
	                uas.setRoundWinShare(           fromBigDecimalToBidecimalAvg(SvcMapStats::getRoundWinShare,             v, 2));
	                uas.setHeadShotsPercentage(     fromBigDecimalToBidecimalAvg(SvcMapStats::getHeadShotsPercentage,       v, 2));
	                uas.setHalfLifeTelevisionRating(fromBigDecimalToBidecimalAvg(SvcMapStats::getHalfLifeTelevisionRating,  v, 3));
	                uas.setKillPerRound(            fromBigDecimalToBidecimalAvg(SvcMapStats::getKillPerRound,              v, 2));
	                uas.setDeathPerRound(           fromBigDecimalToBidecimalAvg(SvcMapStats::getDeathPerRound,             v, 2));
	                uas.setAverageDamagePerRound(   fromBigDecimalToBidecimalAvg(SvcMapStats::getAverageDamagePerRound,     v, 2));
	                uas.setKillDeathRatio(          fromBigDecimalToBidecimalAvg(SvcMapStats::getKillDeathRatio,           v, 2));
	                uas.setKills(                   fromBigDecimalToBidecimalAvg(SvcMapStats::getKills,                   v, 2));
	                uas.setAssists(                 fromBigDecimalToBidecimalAvg(SvcMapStats::getAssists,                 v, 2));
	                uas.setDeaths(                  fromBigDecimalToBidecimalAvg(SvcMapStats::getDeaths,                  v, 2));
	                uas.setHeadShots(               fromBigDecimalToBidecimalAvg(SvcMapStats::getHeadShots,               v, 2));
	                uas.setTeamKillFriendlyFire(    fromBigDecimalToBidecimalAvg(SvcMapStats::getTeamKillFriendlyFire,    v, 2));
	                uas.setEntryKill(               fromBigDecimalToBidecimalAvg(SvcMapStats::getEntryKill,               v, 2));
	                uas.setBombPLanted(             fromBigDecimalToBidecimalAvg(SvcMapStats::getBombPlanted,             v, 2));
	                uas.setBombDefused(             fromBigDecimalToBidecimalAvg(SvcMapStats::getBombDefused,             v, 2));
	                uas.setTradeKill(               fromBigDecimalToBidecimalAvg(SvcMapStats::getTradeKill,               v, 2));
	                uas.setTradeDeath(              fromBigDecimalToBidecimalAvg(SvcMapStats::getTradeDeath,              v, 2));
	                uas.setTotalDamageHealth(       fromBigDecimalToBidecimalAvg(SvcMapStats::getTotalDamageHealth,       v, 2));
	                uas.setTotalDamageArmor(        fromBigDecimalToBidecimalAvg(SvcMapStats::getTotalDamageArmor,        v, 2));
	                uas.setFireDamage(              fromBigDecimalToBidecimalAvg(SvcMapStats::getFireDamage,              v, 2));
	                
	                uas.setOneVersusOne(        fromLongToBidecimalAvg(SvcMapStats::getOneVersusOne,            v, 2));
	                uas.setOneVersusTwo(        fromLongToBidecimalAvg(SvcMapStats::getOneVersusTwo,            v, 2));
	                uas.setOneVersusThree(      fromLongToBidecimalAvg(SvcMapStats::getOneVersusThree,          v, 2));
	                uas.setOneVersusFour(       fromLongToBidecimalAvg(SvcMapStats::getOneVersusFour,           v, 2));
	                uas.setOneVersusFive(       fromLongToBidecimalAvg(SvcMapStats::getOneVersusFive,           v, 2));
	                uas.setMostValuablePlayer(  fromLongToBidecimalAvg(SvcMapStats::getMostValuablePlayer,      v, 2));
	                uas.setScore(               fromLongToBidecimalAvg(SvcMapStats::getScore,                   v, 2));
	                uas.setFiveKills(           fromLongToBidecimalAvg(SvcMapStats::getFiveKills,               v, 2));
	                uas.setFourKills(           fromLongToBidecimalAvg(SvcMapStats::getFourKills,               v, 2));
	                uas.setThreeKills(          fromLongToBidecimalAvg(SvcMapStats::getThreeKills,              v, 2));
	                uas.setTwoKills(            fromLongToBidecimalAvg(SvcMapStats::getTwoKills,                v, 2));
	                uas.setOneKill(             fromLongToBidecimalAvg(SvcMapStats::getOneKill,                 v, 2));
	                
	                //TODO set the values
	                Function<SvcUserAvgScore, BigDecimal> function = null;
	                switch (partionByScore) {
	                case KILLS:
	                    System.out.println("KILLS");
	                    break;
	                case FF:
	                    System.out.println("Team kill friendly fire");
	                    break;
	                case HLTV:
	                    System.out.println("Half life television rating");
	                    break;
	                case BD:
	                    System.out.println("Bomb defuse");
	                    break;
	                case _1V3:
	                    System.out.println("One versus three");
	                    break;
	                case _1V2:
	                    System.out.println("One versus two");
	                    break;
	                case KAST:
	                    System.out.println("Kill, Assist, Survived or Traded");
	                    break;
	                case _1V1:
	                    System.out.println("One versus one");
	                    break;
	                case HR:
	                    System.out.println("Hostage rescued");
	                    break;
	                case BP:
	                    System.out.println("Bomb planted");
	                    break;
	                case UD:
	                    System.out.println("Fire damage");
	                    break;
	                case KAST_TOTAL:
	                    System.out.println("Kast total");
	                    break;
	                case RWS:
	                    System.out.println("Round win share");
	                    break;
	                case SCORE:
	                    System.out.println("Score");
	                    break;
	                case HEAD_SHOTS:
	                    System.out.println("Head shots");
	                    break;
	                case ASSISTS:
	                    System.out.println("Assists");
	                    break;
	                case _4K:
	                    System.out.println("Four kills");
	                    break;
	                case _2K:
	                    System.out.println("Two kills");
	                    break;
	                case _1V5:
	                    System.out.println("One versus five");
	                    break;
	                case _1V4:
	                    System.out.println("One versus four");
	                    break;
	                case HEADSHOT_PERCENTAGE:
	                    System.out.println("Headshot percentage");
	                    break;
	                case DEATHS:
	                    System.out.println("Deaths");
	                    break;
	                case FFD:
	                    System.out.println("Teammate damage health");
	                    break;
	                case EK:
	                    System.out.println("Entry kill");
	                    break;
	                case MVP:
	                    System.out.println("Most valuable player");
	                    break;
	                case DPR:
	                    System.out.println("Death per round");
	                    break;
	                case RWS_TOTAL:
	                    System.out.println("Round win share total");
	                    break;
	                case KPR:
	                    System.out.println("Kill per round");
	                    break;
	                case ADR:
	                    System.out.println("Average damage per round");
	                    break;
	                case TD:
	                    System.out.println("Trade death");
	                    break;
	                case TDA:
	                    System.out.println("Total damage armor");
	                    break;
	                case _5K:
	                    System.out.println("Five kills");
	                    break;
	                case _3K:
	                    System.out.println("Three kills");
	                    break;
	                case EBT:
	                    System.out.println("Opponent blind time");
	                    break;
	                case TK:
	                    System.out.println("Trade kill");
	                    break;
	                case _KDR:
	                    System.out.println("Kill death ratio");
	                    break;
	                case _1K:
	                    System.out.println("One kill");
	                    break;
	                case TDH:
	                    System.out.println("Total damage health");
	                    break;
	                case FBT:
	                    System.out.println("Teammate blind time");
	                    break;
	                case FA:
	                    System.out.println("Flash assists");
	                    break;
	                default:
	                    System.out.println("Unknown ScoreType");
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
	
	
	
	private Mono<Map<String, List<RestPlayerMatchStatsExtended>>> getUserScoresFromDemManagerService(Integer numberOfMatches, List<String> usersIDs, BigDecimal minPercPlayed) throws IxigoException {
		try {
			URL url = new URL(demManagerEndPoints.getGetDemDataUsersScores());
			Map<String, String> queryParams = new HashMap<>();
			queryParams.put(UsersScoresQueryParam.NUMBER_OF_MATCHES.getQueryParamKey(), numberOfMatches.toString());
			queryParams.put(UsersScoresQueryParam.USERS_STEAM_IDS.getQueryParamKey(), String.join(",", usersIDs));
	        
	        _LOGGER.debug(url.toString());
	        _LOGGER.debug(String.format("Query paramas: %s",queryParams.toString()));
	        
			return webClient.performGetRequest(RestUsersScoresResp.class, url, Optional.empty(), Optional.of(queryParams))
					.map(resp -> resp.getBody().getUserScores());
		}
		catch (MalformedURLException e) {
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
		}
		catch (MalformedURLException e) {
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
	
	private Flux<SvcUserAvgScore> getUsersAvg(Integer numberOfMatches, List<String> usersIDs, ScoreType scoreType, BigDecimal minPercPlayed) throws IxigoException {
		Mono<Map<String, SvcUserAvgScore>> usersAvg = this.getUsersAvgStatsForLastXGames(numberOfMatches, usersIDs, scoreType, minPercPlayed);
		return usersAvg.map(map -> map.values()).flatMapMany(Flux::fromIterable);
    }
}
