package com.ixigo.demmanager.services.implementations;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import reactor.core.scheduler.Schedulers;
import reactor.util.function.Tuples;

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
		_LOGGER.trace("Inside DemFileParserImp.processQueuedFiles");
		return repoQueue.getNotProcessedDemFiles().collectList().flatMap(list -> {
			List<File> filesToProcess = list.stream().map(e -> new File(e.getFile_name())).collect(Collectors.toList());
			
			switch (props.getParserExecutionType()) {
			case SYNC:
				return processFiles(filesToProcess).thenReturn(HttpStatus.ACCEPTED);
			case ASYNC:
				new Thread(() -> processFiles(filesToProcess).subscribe(v -> {
					_LOGGER.debug("Async queue process completed");
				})).start();
				return Mono.just(HttpStatus.ACCEPTED);
			default:
				return Mono.just(HttpStatus.ACCEPTED);
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
		} catch (IOException e) {
			e.printStackTrace();
			throw new IxigoException(HttpStatus.INTERNAL_SERVER_ERROR, msgSource.getMessage(ErrorCodes.ERROR_READING_DEM_FILE), ErrorCodes.ERROR_READING_DEM_FILE);
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
	public Flux<SvcUserStatsForLastXGames> getUsersStatsForLastXGames(Integer numberOfMatches, List<String> usersIDs, BigDecimal minPercPlayed) throws IxigoException {
		// @formatter:off
		
		BigDecimal perc = minPercPlayed.divide(BigDecimal.valueOf(100), 2, RoundingMode.DOWN);
		
		// https://stackoverflow.com/questions/70704314/how-to-return-a-reactive-flux-that-contains-a-reactive-mono-and-flux
		return Flux.fromIterable(usersIDs)
			.flatMap(steamId -> repoUser.findById(steamId).defaultIfEmpty(new UsersDto().setSteam_id(steamId)))
			.map(dto -> {
				var list = repoUserScore.getLastXMatchesScoresForUser(numberOfMatches, dto.getSteam_id(), perc)
						.map(dtoScore -> fromUsersScoreDtoToSvcMapStata(dto, dtoScore))
						.collectList();
				return Tuples.of(dto.getSteam_id(), list);
			})
			.flatMap(data -> {
				String steamId = data.getT1();
				Mono<List<SvcMapStats>> monoUserScores = data.getT2();
				
				return monoUserScores.map(list -> {
					var stats = new SvcUserStatsForLastXGames();
					stats.setSteamId(steamId);
					stats.setStats(list);
					return stats;
				});
			})
		;
		// @formatter:on
	}

	private Mono<SvcMapStats> generateMapStatFromFile(File f) throws IxigoException {
		return svcDemProcessor.processDemFile(f).collectList().map(list -> {
			SvcMapStats ms = setMapNameAndTime(f);
			if(list != null && !list.isEmpty()) {
				ms.setUsersStats(list);
			}
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

	private synchronized Mono<Void>  processFiles(List<File> files) {
		AtomicInteger countProcessedFiles = new AtomicInteger(0);
		// @formatter:off
		return Flux.fromIterable(files)
			.parallel()
			.runOn(Schedulers.boundedElastic())
			.flatMap(f -> { // Extracting info from DEM file 
				return generateMapStatFromFile(f).map(stats -> {
					return Tuples.of(f.getAbsolutePath(), stats);
				})
				.onErrorResume(error -> {
					_LOGGER.error(String.format("Error while processing %s, msg: %s", f.getAbsolutePath(), error.getMessage()));
					return Mono.just(Tuples.of(f.getAbsolutePath(), setMapNameAndTime(f)));
				});
			})
			.sequential()// Avoiding running out of DB connections
			.map(tuple -> { // Checking if the DEM file had info in it
				String fileName = tuple.getT1();
				SvcMapStats stats = tuple.getT2();
				boolean canProcessTheFile = stats.getUsersStats() != null; 
				return Tuples.of(canProcessTheFile, fileName, stats);
			})
			.map(tuple -> { // Map the DEM file info into appropriate objects
				boolean canProcess = tuple.getT1();
				String fileName = tuple.getT2();
				List<Users_scoresDto> scoreList = new ArrayList<Users_scoresDto>();
				List<UsersDto> usersList = new ArrayList<UsersDto>();
				
				if(canProcess) {
					SvcMapStats stats = tuple.getT3();
					stats.getUsersStats().forEach(score -> {
						UsersDto user = new UsersDto();
						user.setSteam_id(score.getSteamID());
						user.setUser_name(score.getUserName());
						
						Users_scoresDto userScore = fromUserMapStatsToEntityUserScore(stats, score);
						userScore.setFile_name(fileName);
						
						scoreList.add(userScore);
						usersList.add(user);
					});
				}
				
				return Tuples.of(canProcess, fileName, usersList, scoreList);
			})
			.flatMap(tuple -> { // Saving the objects into the DB
				boolean canProcess = tuple.getT1();
				String fileName = tuple.getT2();
				if(canProcess) {
					List<Mono<?>> monos = new ArrayList<>();
					tuple.getT3().forEach(user ->{
						monos.add(repoUser.insertUpdateUser(user));
					});
					tuple.getT4().forEach(score -> {
						monos.add(repoUserScore.insertUpdateUserScore(score));
					});
					
					/*
					 * I was running out of DB connections, I did not manage
					 * to understand how to re-use the connections / close them
					 * properly... Hence I run them in separate groups...
					 * TODO investigate further when I have time
					 */
					Mono<?> mono = monos.get(0);
					for(int i = 1; i < monos.size(); i++) {
						mono = mono.then(monos.get(i));
					}
					return mono.thenReturn(Tuples.of(fileName, true));
					
					/*
					return Mono.zip(monos, arr -> {
						return true;
					}).thenReturn(Tuples.of(fileName, true));
					*/
				}
				
				return Mono.just(Tuples.of(fileName, false));
			})
			.flatMap(tuple -> { // Updating the queue
				boolean statusOk = tuple.getT2();
				String fileName = tuple.getT1();
				File f = new File(fileName);
				
				if(statusOk) {
					countProcessedFiles.incrementAndGet();
				}else {
					f.delete();
				}
				return setFileProcessed(f, statusOk ? DemProcessStatus.PROCESSED : DemProcessStatus.DELETED).thenReturn(fileName);
			}).map(fileName ->{ // Simple info
				_LOGGER.debug(String.format("Processed file %s", fileName));
				return fileName;
			})
			.collectList()
			.flatMap(list -> {
				return notificationService.sendParsingCompleteNotification(
						"Dem Manager",
						String.format("Processed %d files, %d were successfully processed",
						files.size(),
						countProcessedFiles.get()));
			}).then();
		// @formatter:on
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
