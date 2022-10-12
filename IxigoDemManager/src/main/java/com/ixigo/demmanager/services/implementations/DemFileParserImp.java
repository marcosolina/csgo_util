package com.ixigo.demmanager.services.implementations;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ixigo.demmanager.config.properties.DemFileManagerProps;
import com.ixigo.demmanager.constants.RoundParserUtils;
import com.ixigo.demmanager.enums.DemProcessStatus;
import com.ixigo.demmanager.models.entities.UsersDto;
import com.ixigo.demmanager.models.entities.Users_scoresDto;
import com.ixigo.demmanager.models.svc.demdata.SvcMapPlayedCounter;
import com.ixigo.demmanager.models.svc.demdata.SvcMapStats;
import com.ixigo.demmanager.models.svc.demdata.SvcUser;
import com.ixigo.demmanager.models.svc.demdata.SvcUserGotvScore;
import com.ixigo.demmanager.repositories.interfaces.RepoProcessQueue;
import com.ixigo.demmanager.repositories.interfaces.RepoUser;
import com.ixigo.demmanager.repositories.interfaces.RepoUserScore;
import com.ixigo.demmanager.services.interfaces.DemFileParser;
import com.ixigo.demmanager.services.interfaces.DemProcessor;
import com.ixigo.demmanager.services.interfaces.NotificationService;
import com.ixigo.library.enums.DateFormats;
import com.ixigo.library.errors.IxigoException;
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

	@Override
	public Mono<Boolean> processFiles() throws IxigoException {
		_LOGGER.debug("Processing all the new files files");
		return repoQueue.getNotProcessedDemFiles().collectList().map(list -> {
			List<File> filesToProcess = list.stream().map(e -> new File(e.getFile_name())).collect(Collectors.toList());

			switch (props.getParserExecutionType()) {
			case SYNC:
				processFiles(filesToProcess);
				break;
			default:
				new Thread(() -> processFiles(filesToProcess)).start();
				break;
			}
			return true;
		});
	}

	@Override
	public Mono<Boolean> processAllFiles() throws IxigoException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Mono<Map<String, String>> mapOfAvailableScores() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Flux<SvcMapPlayedCounter> countGamesOnAMap() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Flux<SvcUser> getListOfUsers() throws IxigoException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Mono<Map<String, List<SvcMapStats>>> getUsersStatsForLastXGames(Integer gamesCounter, List<String> usersIDs, BigDecimal minPercPlayed) throws IxigoException {
		// TODO Auto-generated method stub
		return null;
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
}
