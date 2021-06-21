package com.marco.ixigo.demmanager.services.implementations;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.marco.ixigo.demmanager.enums.DemProcessStatus;
import com.marco.ixigo.demmanager.enums.ParserExecutionType;
import com.marco.ixigo.demmanager.enums.ScoreType;
import com.marco.ixigo.demmanager.model.dto.MapStats;
import com.marco.ixigo.demmanager.model.dto.UserMapStats;
import com.marco.ixigo.demmanager.model.entities.DaoMapGamesCounter;
import com.marco.ixigo.demmanager.model.entities.EntityProcessQueue;
import com.marco.ixigo.demmanager.model.entities.EntityUser;
import com.marco.ixigo.demmanager.model.entities.EntityUserScore;
import com.marco.ixigo.demmanager.model.entities.EntityUserScorePk;
import com.marco.ixigo.demmanager.model.rest.demdata.MapPlayed;
import com.marco.ixigo.demmanager.model.rest.demdata.User;
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

    @Value("${com.marco.ixigo.demmanager.demFileManager.rootFolder}")
    private Path root;
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
        List<File> filesToProcess = filesEntities.stream().map(e -> new File(e.getFileName()))
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
        AtomicInteger count = new AtomicInteger();
        files.parallelStream().forEach(f -> {
            try {

                MapStats m = generateMapStatFromFile(f);
                AtomicBoolean ok = new AtomicBoolean();
                m.getUsersStats().stream().forEach(u -> {
                    try {
                        EntityUser user = new EntityUser();
                        user.setSteamId(u.getSteamID());
                        user.setUserName(u.getUserName());
                        repoUser.insertUpdateUser(user);

                        EntityUserScore us = fromUserMapStatsToEntityUserScore(m, u);
                        us.setFileName(f.getAbsolutePath());

                        repoUserScore.insertUpdateUserScore(us);
                        ok.set(ok.get() && true);
                    } catch (Exception e) {
                        setFileProcessed(f, DemProcessStatus.PROCESS_FAILED);
                        String message = String.format("Problem while reading the values extracted from the file: %s",
                                f.getAbsoluteFile());
                        _LOGGER.error(message);
                        notificationService.sendParsingCompleteNotification("Dem Manager", message);
                    }
                });
                if(ok.get()) {
                    setFileProcessed(f, DemProcessStatus.PROCESSED);
                }
            } catch (MarcoException e) {
                setFileProcessed(f, DemProcessStatus.PROCESS_FAILED);
                String message = String.format("Could not process DEM file: %s", f.getAbsoluteFile());
                _LOGGER.error(message);
                notificationService.sendParsingCompleteNotification("Dem Manager", message);
            }
        });
        notificationService.sendParsingCompleteNotification("Dem Manager",
                String.format("Processed %d files", files.size()));
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

    @Override
    public Map<String, String> mapOfAvailableScores() {
        Map<String, String> map = new HashMap<>();
        Arrays.stream(ScoreType.values()).forEach(s -> map.put(s.name(), s.getDesc()));
        return map;
    }

    @Override
    public List<MapPlayed> countGamesOnAMap() {
        List<DaoMapGamesCounter> maps = repoUserScore.getMapsPlayed();
        return maps.stream().map(dao -> {
            MapPlayed mp = new MapPlayed();
            mp.setCount(dao.getCount());
            mp.setMapName(dao.getMapName());
            return mp;
        }).collect(Collectors.toList());
    }

    @Override
    public List<User> getListOfUsers() throws MarcoException {
        return repoUser.getUsers().stream().map(this::fromEntityUserToRestUser).collect(Collectors.toList());
    }

    /**
     * It converts the @{EntityUser} into @{User}
     * 
     * @param entity
     * @return
     */
    private User fromEntityUserToRestUser(EntityUser entity) {
        User u = new User();
        u.setSteamId(entity.getSteamId());
        u.setUserName(entity.getUserName());
        return u;
    }

    @Override
    public Map<String, List<MapStats>> getUsersStatsForLastXGames(Integer gamesCounter, List<String> usersIDs,
            BigDecimal minPercPlayed) throws MarcoException {
        Map<String, List<MapStats>> map = new HashMap<>();

        for (String steamId : usersIDs) {
            EntityUser user = repoUser.findById(steamId);
            if (user == null) {
                map.put(steamId, new ArrayList<>());
                continue;
            }

            List<EntityUserScore> scores = repoUserScore.getLastXUserScores(gamesCounter, steamId, minPercPlayed);
            map.put(steamId, scores.stream().map(s -> fromDbDataToMapStats(user, s)).collect(Collectors.toList()));
        }

        return map;
    }

    /**
     * It uses the @{EntityUser} and @{EntityUserScore} to generate a @{MapStats}
     * 
     * @param user
     * @param score
     * @return
     */
    private MapStats fromDbDataToMapStats(EntityUser user, EntityUserScore score) {
        MapStats ms = new MapStats();
        ms.setMapName(score.getId().getMap());
        ms.setPlayedOn(score.getId().getGameDate());

        UserMapStats ums = new UserMapStats();

        ums.setUserName(user.getUserName());
        ums.setSteamID(user.getSteamId());
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

        ums.setRoundWinShare(RoundParserUtils.bigDecimalToDouble(score.getRoundWinShare(), 2));
        ums.setKillDeathRation(RoundParserUtils.bigDecimalToDouble(score.getKillDeathRation(), 2));
        ums.setHeadShotsPercentage(RoundParserUtils.bigDecimalToDouble(score.getHeadShotsPercentage(), 2));
        ums.setHalfLifeTelevisionRating(RoundParserUtils.bigDecimalToDouble(score.getHalfLifeTelevisionRating(), 3));
        ums.setKillPerRound(RoundParserUtils.bigDecimalToDouble(score.getKillPerRound(), 2));
        ums.setAssistsPerRound(RoundParserUtils.bigDecimalToDouble(score.getAssistsPerRound(), 2));
        ums.setDeathPerRound(RoundParserUtils.bigDecimalToDouble(score.getDeathPerRound(), 2));
        ums.setAverageDamagePerRound(RoundParserUtils.bigDecimalToDouble(score.getAverageDamagePerRound(), 2));
        ums.setMatchPlayed(RoundParserUtils.bigDecimalToDouble(score.getMatchPlayed(), 2));

        ms.addUserMapStats(ums);

        return ms;
    }

    @Override
    public boolean processAllFiles() throws MarcoException {
        List<File> files = new ArrayList<>();
        try (Stream<Path> walk = Files.walk(root)) {
            walk.filter(p -> p.toFile().getName().endsWith(".dem")).map(Path::toFile).forEach(files::add);
        } catch (IOException e) {
            if (_LOGGER.isTraceEnabled()) {
                e.printStackTrace();
            }
            throw new MarcoException(e);
        }

        files.stream().forEach(f -> {
            EntityProcessQueue entity = repoQueue.findById(f.getAbsolutePath());
            if (entity == null) {
                entity = new EntityProcessQueue();
                entity.setFileName(f.getAbsolutePath());
                entity.setProcessStatus(DemProcessStatus.NOT_PROCESSED);
                entity.setQueuedOn(LocalDateTime.now(ZoneOffset.UTC));
                repoQueue.saveEntity(entity);
            }
        });

        return processFiles();
    }

}
