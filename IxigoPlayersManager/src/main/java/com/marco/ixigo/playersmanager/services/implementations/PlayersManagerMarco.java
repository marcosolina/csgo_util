package com.marco.ixigo.playersmanager.services.implementations;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;

import com.marco.ixigo.playersmanager.enums.ScoreType;
import com.marco.ixigo.playersmanager.models.dto.MapStats;
import com.marco.ixigo.playersmanager.models.dto.Team;
import com.marco.ixigo.playersmanager.models.dto.UserAvgScore;
import com.marco.ixigo.playersmanager.models.rest.UsersScores;
import com.marco.ixigo.playersmanager.services.interfaces.PlayersManager;
import com.marco.utils.MarcoException;
import com.marco.utils.network.MarcoNetworkUtils;

public class PlayersManagerMarco implements PlayersManager {
    private static final Logger _LOGGER = LoggerFactory.getLogger(PlayersManagerMarco.class);

    @Autowired
    private MarcoNetworkUtils mnu;
    
    @Override
    public List<Team> generateTeams(Integer teamsCounter, Integer gamesCounter, List<String> usersIDs,
            ScoreType scoreType, BigDecimal minPercPlayed) throws MarcoException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Team> generateTeamsForcingSimilarTeamSizes(Integer teamsCounter, Integer gamesCounter,
            List<String> usersIDs, double penaltyWeigth, ScoreType scoreType, BigDecimal minPercPlayed)
            throws MarcoException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Team> generateTwoTeamsForcingSimilarTeamSizes(Integer gamesCounter, List<String> usersIDs,
            double penaltyWeigth, ScoreType scoreType, BigDecimal minPercPlayed) throws MarcoException {
        // TODO Auto-generated method stub
        return null;
    }
    
    private Map<String, List<MapStats>> getUserScoresFromDemManagerService(Integer gamesCounter, List<String> usersIDs, BigDecimal minPercPlayed) throws MarcoException{
        try {
            URL url = new URL("http", "localhost", 8081, "/demdata/usersscores");
            Map<String, String> queryParam = new HashMap<>();
            queryParam.put("counter", gamesCounter.toString());
            queryParam.put("minPercPlayerd", minPercPlayed.toString());
            
            StringBuilder sb = new StringBuilder();
            usersIDs.stream().forEach(id -> sb.append("," + id));
            if(sb.length() > 0) {
                queryParam.put("userIDs", sb.substring(1));
            }
            
            
            ClientResponse cr = mnu.performGetRequest(url, Optional.empty(), Optional.of(queryParam));
            if(cr.statusCode() != HttpStatus.OK) {
                throw new MarcoException("Not able to retrieve the users scores");
            }
            
            return mnu.getBodyFromResponse(cr, UsersScores.class).getUsersScores();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            _LOGGER.error(e.getMessage());
            throw new MarcoException(e);
        }
    }

    @Override
    public Map<String, UserAvgScore> getUsersAvgStatsForLastXGames(Integer gamesCounter, List<String> usersIDs,
            ScoreType partionByScore, BigDecimal minPercPlayed) throws MarcoException {
        Map<String, UserAvgScore> map = new HashMap<>();

        for (String steamId : usersIDs) {
            EntityUser user = repoUser.findById(steamId);
            if (user == null) {
                map.put(steamId, null);
                continue;
            }

            UserAvgScore uas = new UserAvgScore();
            uas.setSteamID(steamId);
            uas.setUserName(user.getUserName());

            List<EntityUserScore> userRecords = repoUserScore.getLastXUserScores(gamesCounter, steamId, minPercPlayed);
            if(userRecords.isEmpty()) {
                continue;
            }

            // @formatter:off
            uas.setRoundWinShare(           fromBigDecimalToBidecimalAvg(EntityUserScore::getRoundWinShare,             userRecords, 2));
            uas.setKillDeathRatio(          fromBigDecimalToBidecimalAvg(EntityUserScore::getKillDeathRation,           userRecords, 2));
            uas.setHeadShotsPercentage(     fromBigDecimalToBidecimalAvg(EntityUserScore::getHeadShotsPercentage,       userRecords, 2));
            uas.setHalfLifeTelevisionRating(fromBigDecimalToBidecimalAvg(EntityUserScore::getHalfLifeTelevisionRating,  userRecords, 3));
            uas.setKillPerRound(            fromBigDecimalToBidecimalAvg(EntityUserScore::getKillPerRound,              userRecords, 2));
            uas.setAssistsPerRound(         fromBigDecimalToBidecimalAvg(EntityUserScore::getAssistsPerRound,           userRecords, 2));
            uas.setDeathPerRound(           fromBigDecimalToBidecimalAvg(EntityUserScore::getDeathPerRound,             userRecords, 2));
            uas.setAverageDamagePerRound(   fromBigDecimalToBidecimalAvg(EntityUserScore::getAverageDamagePerRound,     userRecords, 2));
            uas.setMatchPlayed(             fromBigDecimalToBidecimalAvg(EntityUserScore::getMatchPlayed,               userRecords, 2));
            
            uas.setKills(               fromLongToBidecimalAvg(EntityUserScore::getKills,                   userRecords, 2));
            uas.setAssists(             fromLongToBidecimalAvg(EntityUserScore::getAssists,                 userRecords, 2));
            uas.setDeaths(              fromLongToBidecimalAvg(EntityUserScore::getDeaths,                  userRecords, 2));
            uas.setHeadShots(           fromLongToBidecimalAvg(EntityUserScore::getHeadShots,               userRecords, 2));
            uas.setTeamKillFriendlyFire(fromLongToBidecimalAvg(EntityUserScore::getTeamKillFriendlyFire,    userRecords, 2));
            uas.setEntryKill(           fromLongToBidecimalAvg(EntityUserScore::getEntryKill,               userRecords, 2));
            uas.setBombPLanted(         fromLongToBidecimalAvg(EntityUserScore::getBombPLanted,             userRecords, 2));
            uas.setBombDefused(         fromLongToBidecimalAvg(EntityUserScore::getBombDefused,             userRecords, 2));
            uas.setMostValuablePlayer(  fromLongToBidecimalAvg(EntityUserScore::getMostValuablePlayer,      userRecords, 2));
            uas.setScore(               fromLongToBidecimalAvg(EntityUserScore::getScore,                   userRecords, 2));
            uas.setFiveKills(           fromLongToBidecimalAvg(EntityUserScore::getFiveKills,               userRecords, 2));
            uas.setFourKills(           fromLongToBidecimalAvg(EntityUserScore::getFourKills,               userRecords, 2));
            uas.setThreeKills(          fromLongToBidecimalAvg(EntityUserScore::getThreeKills,              userRecords, 2));
            uas.setTwoKills(            fromLongToBidecimalAvg(EntityUserScore::getTwoKills,                userRecords, 2));
            uas.setOneKill(             fromLongToBidecimalAvg(EntityUserScore::getOneKill,                 userRecords, 2));
            uas.setTradeKill(           fromLongToBidecimalAvg(EntityUserScore::getTradeKill,               userRecords, 2));
            uas.setTradeDeath(          fromLongToBidecimalAvg(EntityUserScore::getTradeDeath,              userRecords, 2));
            uas.setTotalDamageHealth(   fromLongToBidecimalAvg(EntityUserScore::getTotalDamageHealth,       userRecords, 2));
            uas.setTotalDamageArmor(    fromLongToBidecimalAvg(EntityUserScore::getTotalDamageArmor,        userRecords, 2));
            uas.setOneVersusOne(        fromLongToBidecimalAvg(EntityUserScore::getOneVersusOne,            userRecords, 2));
            uas.setOneVersusTwo(        fromLongToBidecimalAvg(EntityUserScore::getOneVersusTwo,            userRecords, 2));
            uas.setOneVersusThree(      fromLongToBidecimalAvg(EntityUserScore::getOneVersusThree,          userRecords, 2));
            uas.setOneVersusFour(       fromLongToBidecimalAvg(EntityUserScore::getOneVersusFour,           userRecords, 2));
            uas.setOneVersusFive(       fromLongToBidecimalAvg(EntityUserScore::getOneVersusFive,           userRecords, 2));
            uas.setGrenadesThrownCount( fromLongToBidecimalAvg(EntityUserScore::getGrenadesThrownCount,     userRecords, 2));
            uas.setFlashesThrownCount(  fromLongToBidecimalAvg(EntityUserScore::getFlashesThrownCount,      userRecords, 2));
            uas.setSmokesThrownCount(   fromLongToBidecimalAvg(EntityUserScore::getSmokesThrownCount,       userRecords, 2));
            uas.setFireThrownCount(     fromLongToBidecimalAvg(EntityUserScore::getFireThrownCount,         userRecords, 2));
            uas.setHighExplosiveDamage( fromLongToBidecimalAvg(EntityUserScore::getHighExplosiveDamage,     userRecords, 2));
            uas.setFireDamage(          fromLongToBidecimalAvg(EntityUserScore::getFireDamage,              userRecords, 2));
            
            
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

            map.put(steamId, uas);
        }

        return map;
    }

}
