package com.ixigo.demmanager.services.implementations;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import com.ixigo.demmanager.config.properties.DemFileManagerProps;
import com.ixigo.demmanager.constants.ErrorCodes;
import com.ixigo.demmanager.constants.RoundParserUtils;
import com.ixigo.demmanager.enums.DemProcessStatus;
import com.ixigo.demmanager.enums.ScoreType;
import com.ixigo.demmanager.mappers.SvcMapper;
import com.ixigo.demmanager.models.database.Dem_process_queueDto;
import com.ixigo.demmanager.models.database.DtoMapPlayedCounter;
import com.ixigo.demmanager.models.database.UsersDto;
import com.ixigo.demmanager.models.database.Users_scoresDto;
import com.ixigo.demmanager.models.svc.demdata.SvcMapPlayedCounter;
import com.ixigo.demmanager.models.svc.demdata.SvcMapStats;
import com.ixigo.demmanager.models.svc.demdata.SvcUser;
import com.ixigo.demmanager.models.svc.demdata.SvcUserGotvScore;
import com.ixigo.demmanager.models.svc.demdata.SvcUserStatsForLastXGames;
import com.ixigo.demmanager.repositories.interfaces.RepoProcessQueue;
import com.ixigo.demmanager.repositories.interfaces.RepoUser;
import com.ixigo.demmanager.repositories.interfaces.RepoUserScore;
import com.ixigo.demmanager.services.interfaces.DemFileParser;
import com.ixigo.demmanager.services.interfaces.DemProcessor;
import com.ixigo.demmanager.services.interfaces.NotificationService;
import com.ixigo.library.enums.DateFormats;
import com.ixigo.library.errors.IxigoException;
import com.ixigo.library.messages.IxigoMessageResource;
import com.ixigo.library.utils.DateUtils;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class DemFileParserImp implements DemFileParser {
	private static final Logger _LOGGER = LoggerFactory.getLogger(DemFileParserImp.class);

	@Autowired
	private DemFileManagerProps props;
	@Autowired
	private RepoProcessQueue repoQueue;
	@Autowired
	private RepoUser repoUser;
	@Autowired
	private RepoUserScore repoUserScore;
	@Autowired
	private DemProcessor svcDemProcessor;
	@Autowired
	private NotificationService notificationService;
	@Autowired
	private SvcMapper mapper;
	@Autowired
	private IxigoMessageResource msgSource;

	@Override
	public Mono<HttpStatus> processQueuedFiles() throws IxigoException {
		_LOGGER.debug("Processing all the new files files");
		return repoQueue.getNotProcessedDemFiles().collectList().map(list -> {
			List<File> filesToProcess = list.stream().map(e -> new File(e.getFile_name())).collect(Collectors.toList());

			switch (props.getParserExecutionType()) {
			case SYNC:
				processFiles(filesToProcess);
				return HttpStatus.OK;
			default:
				new Thread(() -> processFiles(filesToProcess)).start();
				return HttpStatus.ACCEPTED;
			}
		});
	}

	@Override
	public Mono<HttpStatus> queueAndProcessNewFiles() throws IxigoException {
		
		try {	
			// @formatter:off
			return Flux.fromStream(Files.walk(props.getRootFolder()))
					.filter(path -> path.toFile().getName().endsWith(".dem"))
					.flatMap(path ->
					{
						String absPath = path.toFile().getAbsolutePath();
						return repoQueue.findById(absPath)
								.defaultIfEmpty(new Dem_process_queueDto().setFile_name(absPath));
					})
					.filter(dto -> dto.getProcessed_on() == null)
					.flatMap(dto -> {
						dto.setQueued_on(DateUtils.getCurrentUtcDateTime());
						dto.setProcess_status(DemProcessStatus.NOT_PROCESSED);
						return repoQueue.insertOrUpdate(dto);
					}).then(processQueuedFiles());
			// @formatter:on
		}catch(IOException e) {
			throw new IxigoException(HttpStatus.INTERNAL_SERVER_ERROR, msgSource.getMessage(ErrorCodes.ERROR_READING_DEM_FILE));
		}
	}

	@Override
	public Mono<Map<String, String>> mapOfAvailableScores() {
		Map<String, String> map = new HashMap<>();
		Arrays.stream(ScoreType.values()).forEach(s -> map.put(s.name(), s.getDesc()));
		return Mono.just(map);
	}

	@Override
	public Flux<SvcMapPlayedCounter> countGamesOnAMap() {
		Flux<DtoMapPlayedCounter> maps = repoUserScore.getMapsPlayed();
		return maps.map(dto -> {
			SvcMapPlayedCounter mp = new SvcMapPlayedCounter();
			mp.setCount(dto.getCount());
			mp.setMapName(dto.getMapName());
			return mp;
		});
	}

	@Override
	public Flux<SvcUser> getListOfUsers() throws IxigoException {
		return repoUser.getUsers().map(mapper::fromDtoToSvc);
	}

	@Override
	public Flux<SvcUserStatsForLastXGames> getUsersStatsForLastXGames(Integer gamesCounter, List<String> usersIDs, BigDecimal minPercPlayed) throws IxigoException {
		// @formatter:off
		
		// https://stackoverflow.com/questions/70704314/how-to-return-a-reactive-flux-that-contains-a-reactive-mono-and-flux
		return Flux.fromIterable(usersIDs)
				.flatMap(steamId -> {
					Flux<SvcMapStats> scores = repoUser.findById(steamId).flatMapMany(userDto -> {
						return repoUserScore.getLastXUserScores(gamesCounter, userDto.getSteam_id(), minPercPlayed)
								.map(scoreDto -> fromUsersScoreDtoToSvcMapStata(userDto, scoreDto));
					});
					
					return scores.collectList()
						.zipWith(Mono.just(steamId))
						.map(zip -> {
							var stats = new SvcUserStatsForLastXGames();
							stats.setSteamId(zip.getT2());
							stats.setStats(zip.getT1());
							return stats;
						});
				});
		
		// @formatter:on
	}
	
	

	public Mono<SvcMapStats> generateMapStatFromFile(File f) throws IxigoException {
		return svcDemProcessor.processDemFile(f).collectList().map(list -> {
			SvcMapStats ms = setMapNameAndTime(f);
			ms.setUsersStats(list);
			return ms;
		});
	}

	/**
	 * It will extract the high level dem file info from the file name
	 * 
	 * @param f
	 * @return
	 */
	private SvcMapStats setMapNameAndTime(File f) {
		String[] tmp = f.getName().split("-");
		LocalDateTime ldt = DateUtils.fromStringToLocalDateTime(tmp[1] + "_" + tmp[2], DateFormats.FILE_NAME);
		SvcMapStats ms = new SvcMapStats();
		ms.setPlayedOn(ldt);
		ms.setMapName(tmp[4]);
		return ms;
	}

	private void processFiles(List<File> files) {
		AtomicInteger count = new AtomicInteger(0);
		files.parallelStream().forEach(f -> {
			// files.stream().forEach(f -> {
			try {
				Mono<SvcMapStats> monoStats = generateMapStatFromFile(f);

				monoStats.flatMap(stats -> {
					if (stats.getUsersStats() != null) {
						AtomicBoolean ok = new AtomicBoolean(true);
						stats.getUsersStats().stream().forEach(u -> {
							try {
								UsersDto user = new UsersDto();
								user.setSteam_id(u.getSteamID());
								user.setUser_name(u.getUserName());

								Users_scoresDto userScore = fromUserMapStatsToEntityUserScore(stats, u);
								userScore.setFile_name(f.getAbsolutePath());

								var m1 = repoUser.insertUpdateUser(user);
								var m2 = repoUserScore.insertUpdateUserScore(userScore);
								Mono.zip(m1, m2).then();
							} catch (Exception e) {
								ok.set(false);
								String message = String.format("Problem while reading the values extracted from the file: %s", f.getAbsoluteFile());
								_LOGGER.error(message);

								var m1 = setFileProcessed(f, DemProcessStatus.PROCESS_FAILED);
								var m2 = notificationService.sendParsingCompleteNotification("Dem Manager", message);
								Mono.zip(m1, m2).then();
							}
						});
						if (ok.get()) {
							count.incrementAndGet();
							setFileProcessed(f, DemProcessStatus.PROCESSED).then();
						}
					} else {
						count.incrementAndGet();
						f.delete();
						setFileProcessed(f, DemProcessStatus.DELETED).then();
					}
					return null;
				});

			} catch (IxigoException e) {
				String message = String.format("Could not process DEM file: %s", f.getAbsoluteFile());
				_LOGGER.error(message);

				var m2 = notificationService.sendParsingCompleteNotification("Dem Manager", message);
				var m1 = setFileProcessed(f, DemProcessStatus.PROCESS_FAILED);
				Mono.zip(m1, m2).then();
			}
		});
		notificationService.sendParsingCompleteNotification("Dem Manager", String.format("Processed %d files, %d were successfully processed", files.size(), count.get())).then();
	}

	private Mono<Boolean> setFileProcessed(File f, DemProcessStatus status) {
		return repoQueue.findById(f.getAbsolutePath()).flatMap(dto -> {
			dto.setProcess_status(status);
			dto.setProcessed_on(DateUtils.getCurrentUtcDateTime());
			return repoQueue.insertOrUpdate(dto);
		});
	}

	private Users_scoresDto fromUserMapStatsToEntityUserScore(SvcMapStats ms, SvcUserGotvScore userScore) {
		Users_scoresDto ums = new Users_scoresDto();

		ums.setGame_date(ms.getPlayedOn());
		ums.setMap(ms.getMapName());
		ums.setSteam_id(userScore.getSteamID());

		ums.setKills(userScore.getKills());
		ums.setAssists(userScore.getAssists());
		ums.setDeaths(userScore.getDeaths());
		ums.setTdh(userScore.getTotalDamageHealth());
		ums.setTda(userScore.getTotalDamageArmor());
		ums.set_1v1(userScore.getOneVersusOne());
		ums.set_1v2(userScore.getOneVersusTwo());
		ums.set_1v3(userScore.getOneVersusThree());
		ums.set_1v4(userScore.getOneVersusFour());
		ums.set_1v5(userScore.getOneVersusFive());
		ums.setGrenades(userScore.getGrenadesThrownCount());
		ums.setFlashes(userScore.getFlashesThrownCount());
		ums.setSmokes(userScore.getSmokesThrownCount());
		ums.setFire(userScore.getFireThrownCount());
		ums.setHed(userScore.getHighExplosiveDamage());
		ums.setFd(userScore.getFireDamage());
		ums.set_5k(userScore.getFiveKills());
		ums.set_4k(userScore.getFourKills());
		ums.set_3k(userScore.getThreeKills());
		ums.set_2k(userScore.getTwoKills());
		ums.set_1k(userScore.getOneKill());
		ums.setTk(userScore.getTradeKill());
		ums.setTd(userScore.getTradeDeath());
		ums.setFf(userScore.getTeamKillFriendlyFire());
		ums.setEk(userScore.getEntryKill());
		ums.setBp(userScore.getBombPLanted());
		ums.setBd(userScore.getBombDefused());
		ums.setMvp(userScore.getMostValuablePlayer());
		ums.setScore(userScore.getScore());
		ums.setHs(userScore.getHeadShots());

		ums.setRws(RoundParserUtils.doubleToBigDecimal(userScore.getRoundWinShare(), 2));
		ums.setKdr(RoundParserUtils.doubleToBigDecimal(userScore.getKillDeathRation(), 2));
		ums.setHsp(RoundParserUtils.doubleToBigDecimal(userScore.getHeadShotsPercentage(), 2));
		ums.setHltv(RoundParserUtils.doubleToBigDecimal(userScore.getHalfLifeTelevisionRating(), 3));
		ums.setKpr(RoundParserUtils.doubleToBigDecimal(userScore.getKillPerRound(), 2));
		ums.setApr(RoundParserUtils.doubleToBigDecimal(userScore.getAssistsPerRound(), 2));
		ums.setDpr(RoundParserUtils.doubleToBigDecimal(userScore.getDeathPerRound(), 2));
		ums.setAdr(RoundParserUtils.doubleToBigDecimal(userScore.getAverageDamagePerRound(), 2));
		ums.setMp(RoundParserUtils.doubleToBigDecimal(userScore.getMatchPlayed(), 2));
		return ums;
	}
	
	private SvcMapStats fromUsersScoreDtoToSvcMapStata(UsersDto userDto, Users_scoresDto userScore) {
		SvcMapStats mapStats = new SvcMapStats();
		mapStats.setMapName(userScore.getMap());
		mapStats.setPlayedOn(userScore.getGame_date());
		
		SvcUserGotvScore gotvScore = new SvcUserGotvScore();
		
		gotvScore.setUserName(userDto.getUser_name());
		gotvScore.setSteamID(userDto.getSteam_id());
		gotvScore.setKills(userScore.getKills());
		gotvScore.setAssists(userScore.getAssists());
		gotvScore.setDeaths(userScore.getDeaths());
		gotvScore.setTotalDamageHealth(userScore.getTdh());
		gotvScore.setTotalDamageArmor(userScore.getTda());
		gotvScore.setOneVersusOne(userScore.get_1v1());
		gotvScore.setOneVersusTwo(userScore.get_1v2());
		gotvScore.setOneVersusThree(userScore.get_1v3());
		gotvScore.setOneVersusFour(userScore.get_1v4());
		gotvScore.setOneVersusFive(userScore.get_1v5());
		gotvScore.setGrenadesThrownCount(userScore.getGrenades());
		gotvScore.setFlashesThrownCount(userScore.getFlashes());
		gotvScore.setSmokesThrownCount(userScore.getSmokes());
		gotvScore.setFireThrownCount(userScore.getFire());
		gotvScore.setHighExplosiveDamage(userScore.getHed());
		gotvScore.setFireDamage(userScore.getFd());
		gotvScore.setFiveKills(userScore.get_5k());
		gotvScore.setFourKills(userScore.get_4k());
		gotvScore.setThreeKills(userScore.get_3k());
		gotvScore.setTwoKills(userScore.get_2k());
		gotvScore.setOneKill(userScore.get_1k());
		gotvScore.setTradeKill(userScore.getTk());
		gotvScore.setTradeDeath(userScore.getTd());
		gotvScore.setTeamKillFriendlyFire(userScore.getFf());
		gotvScore.setEntryKill(userScore.getEk());
		gotvScore.setBombPLanted(userScore.getBp());
		gotvScore.setBombDefused(userScore.getBd());
		gotvScore.setMostValuablePlayer(userScore.getMvp());
		gotvScore.setScore(userScore.getScore());
		gotvScore.setHeadShots(userScore.getHs());

		gotvScore.setRoundWinShare(RoundParserUtils.bigDecimalToDouble(userScore.getRws(), 2));
		gotvScore.setKillDeathRation(RoundParserUtils.bigDecimalToDouble(userScore.getKdr(), 2));
		gotvScore.setHeadShotsPercentage(RoundParserUtils.bigDecimalToDouble(userScore.getHsp(), 2));
		gotvScore.setHalfLifeTelevisionRating(RoundParserUtils.bigDecimalToDouble(userScore.getHltv(), 3));
		gotvScore.setKillPerRound(RoundParserUtils.bigDecimalToDouble(userScore.getKpr(), 2));
		gotvScore.setAssistsPerRound(RoundParserUtils.bigDecimalToDouble(userScore.getApr(), 2));
		gotvScore.setDeathPerRound(RoundParserUtils.bigDecimalToDouble(userScore.getDpr(), 2));
		gotvScore.setAverageDamagePerRound(RoundParserUtils.bigDecimalToDouble(userScore.getAdr(), 2));
		gotvScore.setMatchPlayed(RoundParserUtils.bigDecimalToDouble(userScore.getMp(), 2));
		
		mapStats.addUserMapStats(gotvScore);
		return mapStats;
	}
}
