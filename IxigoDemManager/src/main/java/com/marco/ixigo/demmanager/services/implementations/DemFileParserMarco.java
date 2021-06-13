package com.marco.ixigo.demmanager.services.implementations;

import java.io.File;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.marco.ixigo.demmanager.enums.DemProcessStatus;
import com.marco.ixigo.demmanager.enums.ParserExecutionType;
import com.marco.ixigo.demmanager.model.dto.MapStats;
import com.marco.ixigo.demmanager.model.dto.UserMapStats;
import com.marco.ixigo.demmanager.model.entities.EntityProcessQueue;
import com.marco.ixigo.demmanager.model.entities.EntityUser;
import com.marco.ixigo.demmanager.model.entities.EntityUserScore;
import com.marco.ixigo.demmanager.model.entities.EntityUserScorePk;
import com.marco.ixigo.demmanager.repositories.interfaces.RepoProcessQueue;
import com.marco.ixigo.demmanager.repositories.interfaces.RepoUser;
import com.marco.ixigo.demmanager.repositories.interfaces.RepoUserScore;
import com.marco.ixigo.demmanager.services.interfaces.DemFileParser;
import com.marco.ixigo.demmanager.services.interfaces.DemProcessor;
import com.marco.ixigo.demmanager.services.interfaces.NotificationService;
import com.marco.ixigo.demmanager.utils.RoundParserUtils;
import com.marco.utils.DateUtils;
import com.marco.utils.MarcoException;
import com.marco.utils.enums.DateFormats;

public class DemFileParserMarco implements DemFileParser {
    private static final Logger _LOGGER = LoggerFactory.getLogger(DemFileParserMarco.class);

    @Autowired
    private RepoProcessQueue repoQueue;
    @Autowired
    private RepoUser repoUser;
    @Autowired
    private RepoUserScore repoUserScore;
    @Autowired
    private DemProcessor demProcessor;
    @Autowired
    private NotificationService notificationService;
    
    @Value("${com.marco.ixigo.demmanager.demparser.executionType}")
    private ParserExecutionType execType;

    @Override
    public boolean processFiles() throws MarcoException {
        _LOGGER.debug("Processing all the new files files");
        List<EntityProcessQueue> filesEntities = repoQueue.getNotProcessedDemFiles();
        List<File> filesToProcess = filesEntities.stream()
                .map(e ->new File(e.getFileName()))
                .collect(Collectors.toList());
        
        switch (execType) {
        case SYNC:
            processFiles(filesToProcess);
            break;
        default:
            new Thread(() -> processFiles(filesToProcess)).start();
            break;
        }

        return true;
    }
    
    private void processFiles(List<File> files) {
        files.parallelStream()
        .forEach(f -> {
            try {
                
                MapStats m = generateMapStatFromFile(f);
                m.getUsersStats().stream().forEach(u -> {
                    EntityUser user = new EntityUser();
                    user.setSteamId(u.getSteamID());
                    user.setUserName(u.getUserName());
                    repoUser.insertUpdateUser(user);
                    
                    EntityUserScore us = fromUserMapStatsToEntityUserScore(m, u);
                    us.setFileName(f.getAbsolutePath());
                    
                    repoUserScore.insertUpdateUserScore(us);
                });
                setFileProcessed(f, DemProcessStatus.PROCESSED);
            } catch (MarcoException e) {
                setFileProcessed(f, DemProcessStatus.PROCESS_FAILED);
                String message = String.format("Could not process DEM file: %s", f.getAbsoluteFile());
                _LOGGER.error(message);
                notificationService.sendParsingCompleteNotification("Dem Manager", message);
            }
        });
    }
    
    private void setFileProcessed(File f, DemProcessStatus status) {
        EntityProcessQueue entity = repoQueue.findById(f.getAbsolutePath());
        entity.setProcessedOn(LocalDateTime.now(ZoneOffset.UTC));
        entity.setProcessStatus(status);
        repoQueue.saveEntity(entity);
    }
    
    public MapStats generateMapStatFromFile(File f) throws MarcoException {
        MapStats ms = setMapNameAndTime(f);
        ms.setUsersStats(demProcessor.processDemFile(f));
        return ms;
    }
    
    /**
     * It will extract the high level dem file info from the file name
     * 
     * @param f
     * @return
     */
    private MapStats setMapNameAndTime(File f) {
        String[] tmp = f.getName().split("-");

        LocalDateTime ldt = DateUtils.fromStringToLocalDateTime(tmp[1] + "_" + tmp[2], DateFormats.FILE_NAME);
        MapStats ms = new MapStats();
        ms.setPlayedOn(ldt);
        ms.setMapName(tmp[4]);
        return ms;
    }
    
    private EntityUserScore fromUserMapStatsToEntityUserScore(MapStats ms, UserMapStats score) {
        EntityUserScore ums = new EntityUserScore();

        EntityUserScorePk pk = new EntityUserScorePk();
        pk.setGameDate(ms.getPlayedOn());
        pk.setMap(ms.getMapName());
        pk.setSteamId(score.getSteamID());

        ums.setId(pk);
        ums.setKills(score.getKills());
        ums.setAssists(score.getAssists());
        ums.setDeaths(score.getDeaths());
        ums.setTotalDamageHealth(score.getTotalDamageHealth());
        ums.setTotalDamageArmor(score.getTotalDamageArmor());
        ums.setOneVersusOne(score.getOneVersusOne());
        ums.setOneVersusTwo(score.getOneVersusTwo());
        ums.setOneVersusThree(score.getOneVersusThree());
        ums.setOneVersusFour(score.getOneVersusFour());
        ums.setOneVersusFive(score.getOneVersusFive());
        ums.setGrenadesThrownCount(score.getGrenadesThrownCount());
        ums.setFlashesThrownCount(score.getFlashesThrownCount());
        ums.setSmokesThrownCount(score.getSmokesThrownCount());
        ums.setFireThrownCount(score.getFireThrownCount());
        ums.setHighExplosiveDamage(score.getHighExplosiveDamage());
        ums.setFireDamage(score.getFireDamage());
        ums.setFiveKills(score.getFiveKills());
        ums.setFourKills(score.getFourKills());
        ums.setThreeKills(score.getThreeKills());
        ums.setTwoKills(score.getTwoKills());
        ums.setOneKill(score.getOneKill());
        ums.setTradeKill(score.getTradeKill());
        ums.setTradeDeath(score.getTradeDeath());
        ums.setTeamKillFriendlyFire(score.getTeamKillFriendlyFire());
        ums.setEntryKill(score.getEntryKill());
        ums.setBombPLanted(score.getBombPLanted());
        ums.setBombDefused(score.getBombDefused());
        ums.setMostValuablePlayer(score.getMostValuablePlayer());
        ums.setScore(score.getScore());
        ums.setHeadShots(score.getHeadShots());

        ums.setRoundWinShare(RoundParserUtils.doubleToBigDecimal(score.getRoundWinShare(), 2));
        ums.setKillDeathRation(RoundParserUtils.doubleToBigDecimal(score.getKillDeathRation(), 2));
        ums.setHeadShotsPercentage(RoundParserUtils.doubleToBigDecimal(score.getHeadShotsPercentage(), 2));
        ums.setHalfLifeTelevisionRating(RoundParserUtils.doubleToBigDecimal(score.getHalfLifeTelevisionRating(), 3));
        ums.setKillPerRound(RoundParserUtils.doubleToBigDecimal(score.getKillPerRound(), 2));
        ums.setAssistsPerRound(RoundParserUtils.doubleToBigDecimal(score.getAssistsPerRound(), 2));
        ums.setDeathPerRound(RoundParserUtils.doubleToBigDecimal(score.getDeathPerRound(), 2));
        ums.setAverageDamagePerRound(RoundParserUtils.doubleToBigDecimal(score.getAverageDamagePerRound(), 2));
        ums.setMatchPlayed(RoundParserUtils.doubleToBigDecimal(score.getMatchPlayed(), 2));
        return ums;
    }

}
