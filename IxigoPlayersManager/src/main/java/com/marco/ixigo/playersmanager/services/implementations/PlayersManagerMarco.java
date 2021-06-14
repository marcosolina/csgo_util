package com.marco.ixigo.playersmanager.services.implementations;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.function.ToLongFunction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;

import com.marco.ixigo.playersmanager.config.properties.DemManagerServiceProps;
import com.marco.ixigo.playersmanager.enums.ScoreType;
import com.marco.ixigo.playersmanager.models.dto.MapStats;
import com.marco.ixigo.playersmanager.models.dto.Team;
import com.marco.ixigo.playersmanager.models.dto.UserAvgScore;
import com.marco.ixigo.playersmanager.models.dto.UserMapStats;
import com.marco.ixigo.playersmanager.models.rest.Users;
import com.marco.ixigo.playersmanager.models.rest.UsersScores;
import com.marco.ixigo.playersmanager.services.interfaces.PartitionTeams;
import com.marco.ixigo.playersmanager.services.interfaces.PlayersManager;
import com.marco.utils.MarcoException;
import com.marco.utils.network.MarcoNetworkUtils;

public class PlayersManagerMarco implements PlayersManager {
    private static final Logger _LOGGER = LoggerFactory.getLogger(PlayersManagerMarco.class);
    @Autowired
    @Qualifier("Simple")
    private PartitionTeams standardPartition;
    @Autowired
    @Qualifier("IxiGO")
    private PartitionTeams ixigoPartition;
    @Autowired
    private MarcoNetworkUtils mnu;
    @Autowired
    private DemManagerServiceProps demManagerProprs;

    @Override
    public List<Team> generateTeams(Integer teamsCounter, Integer gamesCounter, List<String> usersIDs,
            ScoreType scoreType, BigDecimal minPercPlayed) throws MarcoException {
        List<UserAvgScore> usersList = getUsersAvg(gamesCounter, usersIDs, scoreType, minPercPlayed);

        return standardPartition.partitionTheUsersComparingTheScores(usersList, teamsCounter, 0);
    }

    @Override
    public List<Team> generateTeamsForcingSimilarTeamSizes(Integer teamsCounter, Integer gamesCounter,
            List<String> usersIDs, double penaltyWeigth, ScoreType scoreType, BigDecimal minPercPlayed)
            throws MarcoException {
        List<UserAvgScore> usersList = getUsersAvg(gamesCounter, usersIDs, scoreType, minPercPlayed);
        return standardPartition.partitionTheUsersComparingTheScoresAndTeamMembers(usersList, teamsCounter,
                penaltyWeigth);
    }

    @Override
    public List<Team> generateTwoTeamsForcingSimilarTeamSizes(Integer gamesCounter, List<String> usersIDs,
            double penaltyWeigth, ScoreType scoreType, BigDecimal minPercPlayed) throws MarcoException {
        List<UserAvgScore> usersList = getUsersAvg(gamesCounter, usersIDs, scoreType, minPercPlayed);
        return ixigoPartition.partitionTheUsersComparingTheScores(usersList, 2, penaltyWeigth);
    }

    @Override
    public Map<String, UserAvgScore> getUsersAvgStatsForLastXGames(Integer gamesCounter, List<String> usersIDs,
            ScoreType partionByScore, BigDecimal minPercPlayed) throws MarcoException {
        Map<String, UserAvgScore> map = new HashMap<>();

        Map<String, List<MapStats>> usersScores = this.getUserScoresFromDemManagerService(gamesCounter, usersIDs, minPercPlayed);
        Map<String, String> userDefinitions = this.getUserDefinition();

        usersScores.forEach((k, v) -> {

            UserAvgScore uas = new UserAvgScore();
            uas.setSteamID(k);
            uas.setUserName(userDefinitions.get(k));

            if (!v.isEmpty()) {
            

                // @formatter:off
                uas.setRoundWinShare(           fromBigDecimalToBidecimalAvg(UserMapStats::getRoundWinShare,             v, 2));
                uas.setKillDeathRatio(          fromBigDecimalToBidecimalAvg(UserMapStats::getKillDeathRation,           v, 2));
                uas.setHeadShotsPercentage(     fromBigDecimalToBidecimalAvg(UserMapStats::getHeadShotsPercentage,       v, 2));
                uas.setHalfLifeTelevisionRating(fromBigDecimalToBidecimalAvg(UserMapStats::getHalfLifeTelevisionRating,  v, 3));
                uas.setKillPerRound(            fromBigDecimalToBidecimalAvg(UserMapStats::getKillPerRound,              v, 2));
                uas.setAssistsPerRound(         fromBigDecimalToBidecimalAvg(UserMapStats::getAssistsPerRound,           v, 2));
                uas.setDeathPerRound(           fromBigDecimalToBidecimalAvg(UserMapStats::getDeathPerRound,             v, 2));
                uas.setAverageDamagePerRound(   fromBigDecimalToBidecimalAvg(UserMapStats::getAverageDamagePerRound,     v, 2));
                uas.setMatchPlayed(             fromBigDecimalToBidecimalAvg(UserMapStats::getMatchPlayed,               v, 2));
                
                uas.setKills(               fromLongToBidecimalAvg(UserMapStats::getKills,                   v, 2));
                uas.setAssists(             fromLongToBidecimalAvg(UserMapStats::getAssists,                 v, 2));
                uas.setDeaths(              fromLongToBidecimalAvg(UserMapStats::getDeaths,                  v, 2));
                uas.setHeadShots(           fromLongToBidecimalAvg(UserMapStats::getHeadShots,               v, 2));
                uas.setTeamKillFriendlyFire(fromLongToBidecimalAvg(UserMapStats::getTeamKillFriendlyFire,    v, 2));
                uas.setEntryKill(           fromLongToBidecimalAvg(UserMapStats::getEntryKill,               v, 2));
                uas.setBombPLanted(         fromLongToBidecimalAvg(UserMapStats::getBombPLanted,             v, 2));
                uas.setBombDefused(         fromLongToBidecimalAvg(UserMapStats::getBombDefused,             v, 2));
                uas.setMostValuablePlayer(  fromLongToBidecimalAvg(UserMapStats::getMostValuablePlayer,      v, 2));
                uas.setScore(               fromLongToBidecimalAvg(UserMapStats::getScore,                   v, 2));
                uas.setFiveKills(           fromLongToBidecimalAvg(UserMapStats::getFiveKills,               v, 2));
                uas.setFourKills(           fromLongToBidecimalAvg(UserMapStats::getFourKills,               v, 2));
                uas.setThreeKills(          fromLongToBidecimalAvg(UserMapStats::getThreeKills,              v, 2));
                uas.setTwoKills(            fromLongToBidecimalAvg(UserMapStats::getTwoKills,                v, 2));
                uas.setOneKill(             fromLongToBidecimalAvg(UserMapStats::getOneKill,                 v, 2));
                uas.setTradeKill(           fromLongToBidecimalAvg(UserMapStats::getTradeKill,               v, 2));
                uas.setTradeDeath(          fromLongToBidecimalAvg(UserMapStats::getTradeDeath,              v, 2));
                uas.setTotalDamageHealth(   fromLongToBidecimalAvg(UserMapStats::getTotalDamageHealth,       v, 2));
                uas.setTotalDamageArmor(    fromLongToBidecimalAvg(UserMapStats::getTotalDamageArmor,        v, 2));
                uas.setOneVersusOne(        fromLongToBidecimalAvg(UserMapStats::getOneVersusOne,            v, 2));
                uas.setOneVersusTwo(        fromLongToBidecimalAvg(UserMapStats::getOneVersusTwo,            v, 2));
                uas.setOneVersusThree(      fromLongToBidecimalAvg(UserMapStats::getOneVersusThree,          v, 2));
                uas.setOneVersusFour(       fromLongToBidecimalAvg(UserMapStats::getOneVersusFour,           v, 2));
                uas.setOneVersusFive(       fromLongToBidecimalAvg(UserMapStats::getOneVersusFive,           v, 2));
                uas.setGrenadesThrownCount( fromLongToBidecimalAvg(UserMapStats::getGrenadesThrownCount,     v, 2));
                uas.setFlashesThrownCount(  fromLongToBidecimalAvg(UserMapStats::getFlashesThrownCount,      v, 2));
                uas.setSmokesThrownCount(   fromLongToBidecimalAvg(UserMapStats::getSmokesThrownCount,       v, 2));
                uas.setFireThrownCount(     fromLongToBidecimalAvg(UserMapStats::getFireThrownCount,         v, 2));
                uas.setHighExplosiveDamage( fromLongToBidecimalAvg(UserMapStats::getHighExplosiveDamage,     v, 2));
                uas.setFireDamage(          fromLongToBidecimalAvg(UserMapStats::getFireDamage,              v, 2));
                
                
                Function<UserAvgScore, BigDecimal> function = null;
                switch (partionByScore) {
                case RWS:
                    function = UserAvgScore::getRoundWinShare;
                    break;
                case KILLS:
                    function = UserAvgScore::getKills;
                    break;
                case ASSISTS:
                    function = UserAvgScore::getAssists;
                    break;
                case DEATHS:
                    function = UserAvgScore::getDeaths;
                    break;
                case KDR:
                    function = UserAvgScore::getKillDeathRatio;
                    break;
                case HS:
                    function = UserAvgScore::getHeadShots;
                    break;
                case HSP:
                    function = UserAvgScore::getHeadShotsPercentage;
                    break;
                case FF:
                    function = UserAvgScore::getTeamKillFriendlyFire;
                    break;
                case EK:
                    function = UserAvgScore::getEntryKill;
                    break;
                case BP:
                    function = UserAvgScore::getBombPLanted;
                    break;
                case BD:
                    function = UserAvgScore::getBombDefused;
                    break;
                case MVP:
                    function = UserAvgScore::getMostValuablePlayer;
                    break;
                case SCORE:
                    function = UserAvgScore::getScore;
                    break;
                case HLTV:
                    function = UserAvgScore::getHalfLifeTelevisionRating;
                    break;
                case _5K:
                    function = UserAvgScore::getFiveKills;
                    break;
                case _4K:
                    function = UserAvgScore::getFourKills;
                    break;
                case _3K:
                    function = UserAvgScore::getThreeKills;
                    break;
                case _2K:
                    function = UserAvgScore::getTwoKills;
                    break;
                case _1K:
                    function = UserAvgScore::getOneKill;
                    break;
                case TK:
                    function = UserAvgScore::getTradeKill;
                    break;
                case TD:
                    function = UserAvgScore::getTradeDeath;
                    break;
                case KPR:
                    function = UserAvgScore::getKillPerRound;
                    break;
                case APR:
                    function = UserAvgScore::getAssistsPerRound;
                    break;
                case DPR:
                    function = UserAvgScore::getDeathPerRound;
                    break;
                case ADR:
                    function = UserAvgScore::getAssistsPerRound;
                    break;
                case TDH:
                    function = UserAvgScore::getTotalDamageHealth;
                    break;
                case TDA:
                    function = UserAvgScore::getTotalDamageArmor;
                    break;
                case _1V1:
                    function = UserAvgScore::getOneVersusOne;
                    break;
                case _1V2:
                    function = UserAvgScore::getOneVersusTwo;
                    break;
                case _1V3:
                    function = UserAvgScore::getOneVersusThree;
                    break;
                case _1V4:
                    function = UserAvgScore::getOneVersusFour;
                    break;
                case _1V5:
                    function = UserAvgScore::getOneVersusFive;
                    break;
                case GRENADES:
                    function = UserAvgScore::getGrenadesThrownCount;
                    break;
                case FLASHES:
                    function = UserAvgScore::getFlashesThrownCount;
                    break;
                case SMOKES:
                    function = UserAvgScore::getSmokesThrownCount;
                    break;
                case FIRE:
                    function = UserAvgScore::getFireThrownCount;
                    break;
                case HED:
                    function = UserAvgScore::getHighExplosiveDamage;
                    break;
                case FD:
                    function = UserAvgScore::getFireDamage;
                    break;
                }
                /*
                 * This is the value that will be used to split the teams.
                 * I add ZERO so I get a new object
                 */
                uas.setTeamSplitScore(function.apply(uas).add(BigDecimal.ZERO));
                uas.setOriginalTeamSplitScore(function.apply(uas).add(BigDecimal.ZERO));
                // @formatter:on
            }
            map.put(k, uas);
        });

        return map;
    }

    private BigDecimal fromLongToBidecimalAvg(ToLongFunction<UserMapStats> function, List<MapStats> userRecords,
            int scale) {
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

    private BigDecimal fromBigDecimalToBidecimalAvg(ToDoubleFunction<UserMapStats> function, List<MapStats> userRecords,
            int scale) {
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

    private Map<String, List<MapStats>> getUserScoresFromDemManagerService(Integer gamesCounter, List<String> usersIDs,
            BigDecimal minPercPlayed) throws MarcoException {
        try {
            URL url = new URL(demManagerProprs.getProtocol(), demManagerProprs.getDns(), demManagerProprs.getPort(),
                    demManagerProprs.getGetUsersScores());
            Map<String, String> queryParam = new HashMap<>();
            queryParam.put("counter", gamesCounter.toString());
            queryParam.put("minPercPlayerd", minPercPlayed.toString());

            StringBuilder sb = new StringBuilder();
            usersIDs.stream().forEach(id -> sb.append("," + id));
            if (sb.length() > 0) {
                queryParam.put("userIDs", sb.substring(1));
            }

            ClientResponse cr = mnu.performGetRequest(url, Optional.empty(), Optional.of(queryParam));
            if (cr.statusCode() != HttpStatus.OK) {
                throw new MarcoException("Not able to retrieve the users scores");
            }

            return mnu.getBodyFromResponse(cr, UsersScores.class).getUsersScores();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            _LOGGER.error(e.getMessage());
            throw new MarcoException(e);
        }
    }

    /**
     * It returns a map of the users. The key is the steam ID, the value is the user
     * name
     * 
     * @return
     * @throws MarcoException
     */
    private Map<String, String> getUserDefinition() throws MarcoException {
        try {
            URL url = new URL(demManagerProprs.getProtocol(), demManagerProprs.getDns(), demManagerProprs.getPort(),
                    demManagerProprs.getGetUsers());
            ClientResponse cr = mnu.performGetRequest(url, Optional.empty(), Optional.empty());
            if (cr.statusCode() != HttpStatus.OK) {
                throw new MarcoException("Not able to retrieve the users definition");
            }

            Users users = mnu.getBodyFromResponse(cr, Users.class);
            Map<String, String> usersMap = new HashMap<String, String>();
            users.getUsers().stream().forEach(u -> usersMap.put(u.getSteamId(), u.getUserName()));
            return usersMap;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            _LOGGER.error(e.getMessage());
            throw new MarcoException(e);
        }
    }
    
    private List<UserAvgScore> getUsersAvg(Integer gamesCounter, List<String> usersIDs, ScoreType scoreType, BigDecimal minPercPlayed)
            throws MarcoException {
        Map<String, UserAvgScore> usersAvg = this.getUsersAvgStatsForLastXGames(gamesCounter, usersIDs, scoreType, minPercPlayed);
        List<UserAvgScore> usersList = new ArrayList<>();

        usersAvg.forEach((k, v) -> usersList.add(v));
        return usersList;
    }

}
