package com.ixigo.demmanager.services.implementations;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import com.ixigo.demmanager.config.properties.DemFileManagerProps;
import com.ixigo.demmanager.constants.ErrorCodes;
import com.ixigo.demmanager.enums.DemProcessStatus;
import com.ixigo.demmanager.enums.ScoreType;
import com.ixigo.demmanager.mappers.SvcMapper;
import com.ixigo.demmanager.models.database.Dem_process_queueDto;
import com.ixigo.demmanager.models.database.UsersDto;
import com.ixigo.demmanager.models.database.Users_scoresDto;
import com.ixigo.demmanager.models.svc.demdata.SvcMapStats;
import com.ixigo.demmanager.models.svc.demdata.SvcUser;
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
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

/**
 * My implementation of the @DemFileParser
 * @author marco
 *
 */
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
		// @formatter:off
		return 
			repoQueue.getNotProcessedDemFiles()
				.collectList()
				.flatMap(list -> {
					List<File> filesToProcess = list.stream().map(e -> new File(e.getFile_name())).collect(Collectors.toList());
					
					/*
					 * When testing locally I can configure this to run synchronously 
					 * as I will usually process one or two DEM files.
					 * When running on the Raspberry PI I will process them
					 * synchronously as I will have a lot of DEM files to process. 
					 */
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
		// @formatter:on
	}

	@Override
	public Mono<HttpStatus> queueAndProcessNewFiles() throws IxigoException {

		try {
			// @formatter:off
			return Flux.fromStream(Files.walk(props.getRootFolder()))// Get all the files in the folder
					.filter(path -> path.toFile().getName().endsWith(".dem")) // select only the DEM files
					.flatMap(path ->
					{
						// Check if the DEM file is already saved in the DB queue
						String absPath = path.toFile().getAbsolutePath();
						return repoQueue.findById(absPath)
								.defaultIfEmpty(new Dem_process_queueDto().setFile_name(absPath));
					})
					.filter(dto -> dto.getProcessed_on() == null) // Remove already queued files
					.flatMap(dto -> { // Add the new files to the queue
						dto.setQueued_on(DateUtils.getCurrentUtcDateTime());
						dto.setProcess_status(DemProcessStatus.NOT_PROCESSED);
						return repoQueue.insertOrUpdate(dto);
					}).then(processQueuedFiles()); // Process all the new files
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
	public Flux<SvcUser> getListOfUsers() throws IxigoException {
		return repoUser.getUsers().map(mapper::fromDtoToSvc);
	}

	@Override
	public Flux<SvcUserStatsForLastXGames> getUsersStatsForLastXGames(Integer numberOfMatches, List<String> usersIDs, BigDecimal minPercPlayed) throws IxigoException {
		// @formatter:off
		
		// https://stackoverflow.com/questions/70704314/how-to-return-a-reactive-flux-that-contains-a-reactive-mono-and-flux
		return Flux.fromIterable(usersIDs)
			.flatMap(steamId -> repoUser.findById(steamId).defaultIfEmpty(new UsersDto().setSteam_id(steamId)))// Get the user definition
			.map(dto -> {
				var list = repoUserScore.getLastXMatchesScoresForUser(numberOfMatches, dto.getSteam_id(), minPercPlayed)
						.map(dtoScore -> mapper.fromUsersScoreDtoToSvcMapStata(dto, dtoScore))
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
		String date = tmp[1];
		String time = tmp[2];
		String mapName = tmp[4];
		
		LocalDateTime ldt = DateUtils.fromStringToLocalDateTime(date + "_" + time, DateFormats.FILE_NAME);
		SvcMapStats ms = new SvcMapStats();
		ms.setPlayedOn(ldt);
		ms.setMapName(cleanMapName(mapName));
		return ms;
	}
	
	private String cleanMapName(String dirtyMapName) {
		if(dirtyMapName.toLowerCase().contains("workshop")) {
			int secondUnderscoreIndex = dirtyMapName.indexOf('_', dirtyMapName.indexOf('_') + 1);
			return dirtyMapName.substring(secondUnderscoreIndex + 1);
		}
		return dirtyMapName;
	}

	private synchronized Mono<Void>  processFiles(List<File> files) {
		AtomicInteger countProcessedFiles = new AtomicInteger(0);
		
		// Function to used to extract the data from the DEM file
		Function<? super File, ? extends Publisher<? extends Tuple2<String, SvcMapStats>>> function = f -> { 
			return generateMapStatFromFile(f).map(stats -> {
				return Tuples.of(f.getAbsolutePath(), stats);
			})
			.onErrorResume(error -> {
				_LOGGER.error(String.format("Error while processing %s, msg: %s", f.getAbsolutePath(), error.getMessage()));
				return Mono.just(Tuples.of(f.getAbsolutePath(), setMapNameAndTime(f)));
			});
		};
		
		// @formatter:off
		Flux<File> fileFlux = Flux.fromIterable(files);
		Flux<Tuple2<String, SvcMapStats>> flux = null;
		if(props.getProcessFilesInParallel()) {
			_LOGGER.debug("Parsing dem files in parallel");
			flux = fileFlux.parallel()
					.runOn(Schedulers.boundedElastic())
					.flatMap(function)
					.sequential();
		}else {
			_LOGGER.debug("Parsing dem files synchronously");
			flux = fileFlux.flatMap(function);
		}
		
		return flux.map(tuple -> { // Checking if the DEM file had info in it
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
						
						Users_scoresDto userScore = mapper.fromUserMapStatsToEntityUserScore(stats, score);
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
					_LOGGER.error(String.format("Deleting: %s", fileName));
					//f.delete();
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

	
}
