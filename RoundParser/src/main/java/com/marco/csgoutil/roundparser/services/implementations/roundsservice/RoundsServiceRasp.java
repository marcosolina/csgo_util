package com.marco.csgoutil.roundparser.services.implementations.roundsservice;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import com.marco.csgoutil.roundparser.enums.ParserExecutionType;
import com.marco.csgoutil.roundparser.model.entities.DaoGames;
import com.marco.csgoutil.roundparser.model.entities.EntityUser;
import com.marco.csgoutil.roundparser.model.entities.EntityUserScore;
import com.marco.csgoutil.roundparser.model.entities.EntityUserScorePk;
import com.marco.csgoutil.roundparser.model.rest.User;
import com.marco.csgoutil.roundparser.model.rest.UserAvgScore;
import com.marco.csgoutil.roundparser.model.service.MapStats;
import com.marco.csgoutil.roundparser.model.service.Team;
import com.marco.csgoutil.roundparser.model.service.UserMapStats;
import com.marco.csgoutil.roundparser.repositories.interfaces.RepoUser;
import com.marco.csgoutil.roundparser.repositories.interfaces.RepoUserScore;
import com.marco.csgoutil.roundparser.services.interfaces.CsgoRoundFileParser;
import com.marco.csgoutil.roundparser.services.interfaces.PartitionTeams;
import com.marco.csgoutil.roundparser.services.interfaces.RoundFileService;
import com.marco.csgoutil.roundparser.services.interfaces.RoundsService;
import com.marco.utils.DateUtils;
import com.marco.utils.MarcoException;
import com.marco.utils.enums.DateFormats;

/**
 * My rasp is quite slow, so I have provided an implementation which runs some
 * logic in a separate thread, otherwise the rest request will time out
 * 
 * @author Marco
 *
 */
public class RoundsServiceRasp implements RoundsService {
	private static final Logger _LOGGER = LoggerFactory.getLogger(RoundsServiceRasp.class);

	@Autowired
	private RoundFileService roundFildeService;
	@Autowired
	private CsgoRoundFileParser roundParserService;
	@Autowired
	private RepoUser repoUser;
	@Autowired
	private RepoUserScore repoUserScore;
	@Autowired
	@Qualifier("Simple")
	private PartitionTeams standardPartition;
	@Autowired
	@Qualifier("IxiGO")
	private PartitionTeams ixigoPartition;
	@Autowired
	private MessageSource msgSource;
	@Value("${com.marco.csgoutil.roundparser.deleteBadDemFiles}")
	private boolean deleteBadDemFiles;
	@Value("${com.marco.csgoutil.roundparser.executionType}")
	private ParserExecutionType executionType;

	@Override
	public List<MapStats> processNewDemFiles() throws MarcoException {
		
		
		List<MapStats> mapStats = new ArrayList<>();
		Runnable parser = () -> {
			try {
				// Get the list of all the available dem files
				List<File> fileList = roundFildeService.retrieveAllDemFiles();
				// Get the list of the dem files that I have already processed
				List<DaoGames> availableRecordings = repoUserScore.listAvailableGames();

				// @formatter:off
				// Parse only the new dem files
				fileList.parallelStream()
					.filter(f -> !availableRecordings.contains(new DaoGames(setMapNameAndTime(f).getPlayedOn())))
					.forEach(f -> {
						try {
							mapStats.add(generateMapStatFromFile(f));
						} catch (MarcoException e) {
							_LOGGER.error(String.format("Could not process DEM file: %s", f.getAbsoluteFile()));
							if(deleteBadDemFiles) {
								_LOGGER.info(String.format("File deleted: %b", f.delete()));
							}
						}
					});
				// @formatter:on

				mapStats.sort((o1, o2) -> o1.getPlayedOn().compareTo(o2.getPlayedOn()));

				// @formatter:off
				// Store into the DB the new dem files info
				mapStats.stream().forEach(m -> 
					m.getUsersStats().stream().forEach(u -> {
						EntityUser user = new EntityUser();
						user.setSteamId(u.getSteamID());
						user.setUserName(u.getUserName());
						repoUser.insertUpdateUser(user);
						
						EntityUserScore us = fromUserMapStatsToEntityUserScore(m, u);
						
						repoUserScore.insertUpdateUserScore(us);
					})
				);
				// @formatter:on
				_LOGGER.info("Files parsing completed");
			} catch (MarcoException e1) {
				_LOGGER.error(e1.getMessage());
			}
		};
		
		
		Thread thread = new Thread(parser);
		thread.start();
		if(executionType != ParserExecutionType.ASYNC) {
			try {
				thread.join();
				return mapStats;
			} catch (InterruptedException e) {
				if(_LOGGER.isTraceEnabled()) {
					e.printStackTrace();
				}
				throw new MarcoException(e);
			}
		}

		return new ArrayList<>();
	}

	@Override
	public MapStats generateMapStatFromFile(File f) throws MarcoException {
		MapStats ms = setMapNameAndTime(f);
		ms.setUsersStats(roundParserService.extractPlayersScore(f));
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
	public List<MapStats> getUserStats(String steamId) throws MarcoException {
		EntityUser user = repoUser.findById(steamId);
		if (user == null) {
			throw new MarcoException(msgSource.getMessage("DEMP00001", null, LocaleContextHolder.getLocale()));
		}

		List<EntityUserScore> scores = repoUserScore.getUserScores(steamId);

		return scores.stream().map(s -> fromDbDataToMapStats(user, s)).collect(Collectors.toList());
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
		ums.setRoundWinShare(score.getRoundWinShare());
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
		
		ums.setKillDeathRation(score.getKillDeathRation());
		ums.setHeadShotsPercentage(score.getHeadShotsPercentage());
		ums.setHalfLifeTelevisionRating(score.getHalfLifeTelevisionRating());
		ums.setKillPerRound(score.getKillPerRound());
		ums.setAssistsPerRound(score.getAssistsPerRound());
		ums.setDeathPerRound(score.getDeathPerRound());
		ums.setAverageDamagePerRound(score.getAverageDamagePerRound());
		
		
		ms.addUserMapStats(ums);

		return ms;
	}
	
	private EntityUserScore fromUserMapStatsToEntityUserScore(MapStats ms, UserMapStats score) {
		EntityUserScore ums = new EntityUserScore();
		
		EntityUserScorePk pk = new EntityUserScorePk();
		pk.setGameDate(ms.getPlayedOn());
		pk.setMap(ms.getMapName());
		pk.setSteamId(score.getSteamID());
		
		ums.setId(pk);
		ums.setRoundWinShare(score.getRoundWinShare());
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
		
		ums.setKillDeathRation(score.getKillDeathRation());
		ums.setHeadShotsPercentage(score.getHeadShotsPercentage());
		ums.setHalfLifeTelevisionRating(score.getHalfLifeTelevisionRating());
		ums.setKillPerRound(score.getKillPerRound());
		ums.setAssistsPerRound(score.getAssistsPerRound());
		ums.setDeathPerRound(score.getDeathPerRound());
		ums.setAverageDamagePerRound(score.getAverageDamagePerRound());
		return ums;
	}

	@Override
	public Map<String, List<MapStats>> getUsersStatsForLastXGames(Integer gamesCounter, List<String> usersIDs)
			throws MarcoException {
		Map<String, List<MapStats>> map = new HashMap<>();

		for (String steamId : usersIDs) {
			EntityUser user = repoUser.findById(steamId);
			if (user == null) {
				map.put(steamId, new ArrayList<>());
				continue;
			}

			List<EntityUserScore> scores = repoUserScore.getLastXUserScores(gamesCounter, steamId);
			map.put(steamId, scores.stream().map(s -> fromDbDataToMapStats(user, s)).collect(Collectors.toList()));
		}

		return map;
	}

	@Override
	public Map<String, UserAvgScore> getUsersAvgStatsForLastXGames(Integer gamesCounter, List<String> usersIDs)
			throws MarcoException {

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

			List<Double> scores = repoUserScore.getLastXUserScoresValue(gamesCounter, steamId);
			Double avg = scores.stream().mapToDouble(a -> a).average().orElse(0);
			uas.setAvgScore(BigDecimal.valueOf(avg).setScale(2, RoundingMode.DOWN));
			uas.setOriginalAvgScore(BigDecimal.valueOf(avg).setScale(2, RoundingMode.DOWN));

			map.put(steamId, uas);
		}

		return map;
	}

	@Override
	public List<Team> generateTeams(Integer teamsCounter, Integer gamesCounter, List<String> usersIDs)
			throws MarcoException {
		List<UserAvgScore> usersList = getUsersAvg(gamesCounter, usersIDs);

		return standardPartition.partitionTheUsersComparingTheScores(usersList, teamsCounter, 0);
	}

	@Override
	public List<Team> generateTeamsForcingSimilarTeamSizes(Integer teamsCounter, Integer gamesCounter,
			List<String> usersIDs, double penaltyWeigth) throws MarcoException {
		List<UserAvgScore> usersList = getUsersAvg(gamesCounter, usersIDs);
		return standardPartition.partitionTheUsersComparingTheScoresAndTeamMembers(usersList, teamsCounter, penaltyWeigth);
	}

	@Override
	public List<LocalDateTime> getAvailableGamesList() {
		return repoUserScore.listAvailableGames().stream().map(DaoGames::getGameOf).collect(Collectors.toList());
	}

	@Override
	public List<Team> generateTwoTeamsForcingSimilarTeamSizes(Integer gamesCounter,
			List<String> usersIDs, double penaltyWeigth) throws MarcoException {
		List<UserAvgScore> usersList = getUsersAvg(gamesCounter, usersIDs);
		
		return ixigoPartition.partitionTheUsersComparingTheScores(usersList, 2, penaltyWeigth);
	}
	
	private List<UserAvgScore> getUsersAvg(Integer gamesCounter, List<String> usersIDs) throws MarcoException{
		Map<String, UserAvgScore> usersAvg = this.getUsersAvgStatsForLastXGames(gamesCounter, usersIDs);
		List<UserAvgScore> usersList = new ArrayList<>();

		usersAvg.forEach((k, v) -> usersList.add(v));
		return usersList;
	}

}
