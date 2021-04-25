package com.marco.csgoutil.roundparser.services.implementations.roundsservice;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.ToLongFunction;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import com.marco.csgoutil.roundparser.enums.ParserExecutionType;
import com.marco.csgoutil.roundparser.enums.ScoreType;
import com.marco.csgoutil.roundparser.model.entities.DaoGames;
import com.marco.csgoutil.roundparser.model.entities.DaoMapPlayed;
import com.marco.csgoutil.roundparser.model.entities.EntityUser;
import com.marco.csgoutil.roundparser.model.entities.EntityUserScore;
import com.marco.csgoutil.roundparser.model.entities.EntityUserScorePk;
import com.marco.csgoutil.roundparser.model.rest.players.MapPlayed;
import com.marco.csgoutil.roundparser.model.rest.players.User;
import com.marco.csgoutil.roundparser.model.rest.players.UserAvgScore;
import com.marco.csgoutil.roundparser.model.service.MapStats;
import com.marco.csgoutil.roundparser.model.service.Team;
import com.marco.csgoutil.roundparser.model.service.UserMapStats;
import com.marco.csgoutil.roundparser.repositories.interfaces.RepoUser;
import com.marco.csgoutil.roundparser.repositories.interfaces.RepoUserScore;
import com.marco.csgoutil.roundparser.services.interfaces.CsgoRoundFileParser;
import com.marco.csgoutil.roundparser.services.interfaces.NotificationService;
import com.marco.csgoutil.roundparser.services.interfaces.PartitionTeams;
import com.marco.csgoutil.roundparser.services.interfaces.RoundFileService;
import com.marco.csgoutil.roundparser.services.interfaces.RoundsService;
import com.marco.csgoutil.roundparser.utils.RoundParserUtils;
import com.marco.utils.DateUtils;
import com.marco.utils.MarcoException;
import com.marco.utils.enums.DateFormats;

/**
 * This implementation provides the logic required when parsing the dem files
 * 
 * @author Marco
 *
 */
public class RoundsServiceMarco implements RoundsService {
	private static final Logger _LOGGER = LoggerFactory.getLogger(RoundsServiceMarco.class);

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
	@Autowired
    private ApplicationContext appContext;

	@Override
	public List<MapStats> processNewDemFiles(boolean forceDeleteBadFiles) throws MarcoException {

		List<MapStats> mapStats = new ArrayList<>();
		Map<String, NotificationService> notificationBeans = appContext.getBeansOfType(NotificationService.class);
		
		Runnable parser = () -> {
			try {
				// Get the list of all the available dem files
				List<File> fileList = roundFildeService.retrieveAllDemFiles();
				// Get the list of the dem files that I have already processed
				List<DaoGames> availableRecordings = repoUserScore.listAvailableGames();

				LocalDateTime start = LocalDateTime.now();
				// @formatter:off
				// Parse only the new dem files
				fileList.parallelStream()
					.filter(f -> !availableRecordings.contains(new DaoGames(setMapNameAndTime(f).getPlayedOn())))
					.forEach(f -> {
						try {
							
							MapStats m = generateMapStatFromFile(f);
							m.getUsersStats().stream().forEach(u -> {
								EntityUser user = new EntityUser();
								user.setSteamId(u.getSteamID());
								user.setUserName(u.getUserName());
								repoUser.insertUpdateUser(user);
								
								EntityUserScore us = fromUserMapStatsToEntityUserScore(m, u);
								
								repoUserScore.insertUpdateUserScore(us);
							});
							mapStats.add(m);
						} catch (MarcoException e) {
						    String message = String.format("Could not process DEM file: %s", f.getAbsoluteFile());
							_LOGGER.error(message);
							notificationBeans.forEach((n, b) -> b.sendParsingCompleteNotification("CSGO - Dem Files Process", message));
							if(deleteBadDemFiles || forceDeleteBadFiles) {
								_LOGGER.info(String.format("File deleted: %b", f.delete()));
							}
						}
					});
				// @formatter:on
				LocalDateTime end = LocalDateTime.now();
				
				long seconds = Duration.between(start, end).getSeconds();
			    long absSeconds = Math.abs(seconds);
			    String duration = String.format(
			        "%d:%02d:%02d",
			        absSeconds / 3600,
			        (absSeconds % 3600) / 60,
			        absSeconds % 60);
				
				StringBuilder message = new StringBuilder();
				message.append(String.format("%d new files were processed ", mapStats.size()));
				message.append("in " + duration);
				message.append("\n\nCheck the new stats at https://marco.selfip.net/cstrike/");
				
				notificationBeans.forEach((n, b) -> b.sendParsingCompleteNotification("CSGO - Dem Files Processed", message.toString()));
				
				_LOGGER.info("Files parsing completed");
			} catch (MarcoException e1) {
				_LOGGER.error(e1.getMessage());
			}
		};

		Thread thread = new Thread(parser);
		thread.start();
		if (executionType != ParserExecutionType.ASYNC) {
			try {
				thread.join();
				return mapStats;
			} catch (InterruptedException e) {
				if (_LOGGER.isTraceEnabled()) {
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
	public Map<String, List<MapStats>> getUsersStatsForLastXGames(Integer gamesCounter, List<String> usersIDs, BigDecimal minPercPlayed)
			throws MarcoException {
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
			uas.setRoundWinShare(			fromBigDecimalToBidecimalAvg(EntityUserScore::getRoundWinShare,				userRecords, 2));
			uas.setKillDeathRatio(			fromBigDecimalToBidecimalAvg(EntityUserScore::getKillDeathRation,			userRecords, 2));
			uas.setHeadShotsPercentage(		fromBigDecimalToBidecimalAvg(EntityUserScore::getHeadShotsPercentage,		userRecords, 2));
			uas.setHalfLifeTelevisionRating(fromBigDecimalToBidecimalAvg(EntityUserScore::getHalfLifeTelevisionRating,	userRecords, 3));
			uas.setKillPerRound(			fromBigDecimalToBidecimalAvg(EntityUserScore::getKillPerRound,				userRecords, 2));
			uas.setAssistsPerRound(			fromBigDecimalToBidecimalAvg(EntityUserScore::getAssistsPerRound,			userRecords, 2));
			uas.setDeathPerRound(			fromBigDecimalToBidecimalAvg(EntityUserScore::getDeathPerRound,				userRecords, 2));
			uas.setAverageDamagePerRound(	fromBigDecimalToBidecimalAvg(EntityUserScore::getAverageDamagePerRound,		userRecords, 2));
			uas.setMatchPlayed(				fromBigDecimalToBidecimalAvg(EntityUserScore::getMatchPlayed,				userRecords, 2));
			
			uas.setKills(				fromLongToBidecimalAvg(EntityUserScore::getKills,					userRecords, 2));
			uas.setAssists(				fromLongToBidecimalAvg(EntityUserScore::getAssists,					userRecords, 2));
			uas.setDeaths(				fromLongToBidecimalAvg(EntityUserScore::getDeaths,					userRecords, 2));
			uas.setHeadShots(			fromLongToBidecimalAvg(EntityUserScore::getHeadShots,				userRecords, 2));
			uas.setTeamKillFriendlyFire(fromLongToBidecimalAvg(EntityUserScore::getTeamKillFriendlyFire, 	userRecords, 2));
			uas.setEntryKill(			fromLongToBidecimalAvg(EntityUserScore::getEntryKill,				userRecords, 2));
			uas.setBombPLanted(			fromLongToBidecimalAvg(EntityUserScore::getBombPLanted,				userRecords, 2));
			uas.setBombDefused(			fromLongToBidecimalAvg(EntityUserScore::getBombDefused,				userRecords, 2));
			uas.setMostValuablePlayer(	fromLongToBidecimalAvg(EntityUserScore::getMostValuablePlayer,		userRecords, 2));
			uas.setScore(				fromLongToBidecimalAvg(EntityUserScore::getScore,					userRecords, 2));
			uas.setFiveKills(			fromLongToBidecimalAvg(EntityUserScore::getFiveKills,				userRecords, 2));
			uas.setFourKills(			fromLongToBidecimalAvg(EntityUserScore::getFourKills,				userRecords, 2));
			uas.setThreeKills(			fromLongToBidecimalAvg(EntityUserScore::getThreeKills,				userRecords, 2));
			uas.setTwoKills(			fromLongToBidecimalAvg(EntityUserScore::getTwoKills,				userRecords, 2));
			uas.setOneKill(				fromLongToBidecimalAvg(EntityUserScore::getOneKill,					userRecords, 2));
			uas.setTradeKill(			fromLongToBidecimalAvg(EntityUserScore::getTradeKill,				userRecords, 2));
			uas.setTradeDeath(			fromLongToBidecimalAvg(EntityUserScore::getTradeDeath,				userRecords, 2));
			uas.setTotalDamageHealth(	fromLongToBidecimalAvg(EntityUserScore::getTotalDamageHealth,		userRecords, 2));
			uas.setTotalDamageArmor(	fromLongToBidecimalAvg(EntityUserScore::getTotalDamageArmor,		userRecords, 2));
			uas.setOneVersusOne(		fromLongToBidecimalAvg(EntityUserScore::getOneVersusOne,			userRecords, 2));
			uas.setOneVersusTwo(		fromLongToBidecimalAvg(EntityUserScore::getOneVersusTwo,			userRecords, 2));
			uas.setOneVersusThree(		fromLongToBidecimalAvg(EntityUserScore::getOneVersusThree,			userRecords, 2));
			uas.setOneVersusFour(		fromLongToBidecimalAvg(EntityUserScore::getOneVersusFour,			userRecords, 2));
			uas.setOneVersusFive(		fromLongToBidecimalAvg(EntityUserScore::getOneVersusFive,			userRecords, 2));
			uas.setGrenadesThrownCount(	fromLongToBidecimalAvg(EntityUserScore::getGrenadesThrownCount,		userRecords, 2));
			uas.setFlashesThrownCount(	fromLongToBidecimalAvg(EntityUserScore::getFlashesThrownCount,		userRecords, 2));
			uas.setSmokesThrownCount(	fromLongToBidecimalAvg(EntityUserScore::getSmokesThrownCount,		userRecords, 2));
			uas.setFireThrownCount(		fromLongToBidecimalAvg(EntityUserScore::getFireThrownCount,			userRecords, 2));
			uas.setHighExplosiveDamage(	fromLongToBidecimalAvg(EntityUserScore::getHighExplosiveDamage,		userRecords, 2));
			uas.setFireDamage(			fromLongToBidecimalAvg(EntityUserScore::getFireDamage,				userRecords, 2));
			
			
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

	private BigDecimal fromLongToBidecimalAvg(ToLongFunction<EntityUserScore> function,
			List<EntityUserScore> userRecords, int scale) {
		return BigDecimal.valueOf(userRecords.stream().mapToDouble(function::applyAsLong).average().orElse(0))
				.setScale(scale, RoundingMode.DOWN);
	}

	private BigDecimal fromBigDecimalToBidecimalAvg(Function<EntityUserScore, BigDecimal> function,
			List<EntityUserScore> userRecords, int scale) {
		return BigDecimal
				.valueOf(userRecords.stream().mapToDouble(el -> function.apply(el).doubleValue()).average().orElse(0))
				.setScale(scale, RoundingMode.DOWN);
	}

	@Override
	public List<Team> generateTeams(Integer teamsCounter, Integer gamesCounter, List<String> usersIDs,
			ScoreType scoreType, BigDecimal minPercPlayed) throws MarcoException {
		List<UserAvgScore> usersList = getUsersAvg(gamesCounter, usersIDs, scoreType, minPercPlayed);

		return standardPartition.partitionTheUsersComparingTheScores(usersList, teamsCounter, 0);
	}

	@Override
	public List<Team> generateTeamsForcingSimilarTeamSizes(Integer teamsCounter, Integer gamesCounter,
			List<String> usersIDs, double penaltyWeigth, ScoreType scoreType, BigDecimal minPercPlayed) throws MarcoException {
		List<UserAvgScore> usersList = getUsersAvg(gamesCounter, usersIDs, scoreType, minPercPlayed);
		return standardPartition.partitionTheUsersComparingTheScoresAndTeamMembers(usersList, teamsCounter,
				penaltyWeigth);
	}

	@Override
	public List<LocalDateTime> getAvailableGamesList() {
		return repoUserScore.listAvailableGames().stream().map(DaoGames::getGameOf).collect(Collectors.toList());
	}

	@Override
	public List<Team> generateTwoTeamsForcingSimilarTeamSizes(Integer gamesCounter, List<String> usersIDs,
			double penaltyWeigth, ScoreType scoreType, BigDecimal minPercPlayed) throws MarcoException {
		List<UserAvgScore> usersList = getUsersAvg(gamesCounter, usersIDs, scoreType, minPercPlayed);

		return ixigoPartition.partitionTheUsersComparingTheScores(usersList, 2, penaltyWeigth);
	}

	private List<UserAvgScore> getUsersAvg(Integer gamesCounter, List<String> usersIDs, ScoreType scoreType, BigDecimal minPercPlayed)
			throws MarcoException {
		Map<String, UserAvgScore> usersAvg = this.getUsersAvgStatsForLastXGames(gamesCounter, usersIDs, scoreType, minPercPlayed);
		List<UserAvgScore> usersList = new ArrayList<>();

		usersAvg.forEach((k, v) -> usersList.add(v));
		return usersList;
	}

	@Override
	public Map<String, String> mapOfAvailableScores() {
		Map<String, String> map = new HashMap<>();
		Arrays.stream(ScoreType.values()).forEach(s -> map.put(s.name(), s.getDesc()));
		return map;
	}

    @Override
    public List<MapPlayed> countGamesOnAMap() {
        List<DaoMapPlayed> maps = repoUserScore.getMapsPlayed();
        return maps.stream().map(dao -> {
            MapPlayed mp = new MapPlayed();
            mp.setCount(dao.getCount());
            mp.setMapName(dao.getMapName());
            return mp;
        }).collect(Collectors.toList());
    }

}
