package com.ixigo.demmanager.services.implementations;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
import com.ixigo.demmanager.mappers.SvcMapper;
import com.ixigo.demmanager.misc.Utils;
import com.ixigo.demmanager.models.database.Dem_process_queueDto;
import com.ixigo.demmanager.models.database.Match_statsDao;
import com.ixigo.demmanager.models.database.Match_statsDto;
import com.ixigo.demmanager.models.database.Player_round_statsDao;
import com.ixigo.demmanager.models.database.Player_statsDao;
import com.ixigo.demmanager.models.database.Round_eventsDao;
import com.ixigo.demmanager.models.database.Round_hit_eventsDao;
import com.ixigo.demmanager.models.database.Round_kill_eventsDao;
import com.ixigo.demmanager.models.database.Round_shot_eventsDao;
import com.ixigo.demmanager.models.database.Round_statsDao;
import com.ixigo.demmanager.models.database.UsersDto;
import com.ixigo.demmanager.models.svc.demdata.data.SvcMatchStats;
import com.ixigo.demmanager.models.svc.demdata.data.SvcPlayerMatchStatsExtended;
import com.ixigo.demmanager.models.svc.demdata.data.SvcPlayerRoundStats;
import com.ixigo.demmanager.models.svc.demdata.data.SvcPlayerStats;
import com.ixigo.demmanager.models.svc.demdata.data.SvcRoundEvents;
import com.ixigo.demmanager.models.svc.demdata.data.SvcRoundHitEvents;
import com.ixigo.demmanager.models.svc.demdata.data.SvcRoundKillEvents;
import com.ixigo.demmanager.models.svc.demdata.data.SvcRoundShotEvents;
import com.ixigo.demmanager.models.svc.demdata.data.SvcRoundStats;
import com.ixigo.demmanager.models.svc.demdata.data.SvcUsers;
import com.ixigo.demmanager.models.svc.demdata.nodejs.SvcNodeJsParseOutput;
import com.ixigo.demmanager.models.svc.demdata.responses.SvcUserStatsForLastXGames;
import com.ixigo.demmanager.repositories.interfaces.CrudRepo;
import com.ixigo.demmanager.repositories.interfaces.RepoProcessQueue;
import com.ixigo.demmanager.repositories.interfaces.RepoUser;
import com.ixigo.demmanager.repositories.interfaces.RepoUserScore;
import com.ixigo.demmanager.services.interfaces.DemFileParser;
import com.ixigo.demmanager.services.interfaces.DemProcessor;
import com.ixigo.demmanager.services.interfaces.NotificationService;
import com.ixigo.eventdispatcher.enums.EventType;
import com.ixigo.library.errors.IxigoException;
import com.ixigo.library.messages.IxigoMessageResource;
import com.ixigo.library.rest.interfaces.IxigoEventSender;
import com.ixigo.library.utils.DateUtils;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

/**
 * My implementation of the @DemFileParser
 * 
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
	private CrudRepo genericRepo;
	@Autowired
	private DemProcessor svcDemProcessor;
	@Autowired
	private NotificationService notificationService;
	@Autowired
	private SvcMapper mapper;
	@Autowired
	private IxigoMessageResource msgSource;
	@Autowired
	private IxigoEventSender eventSender;

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
						//return processFiles(filesToProcess).thenReturn(HttpStatus.ACCEPTED);
						new Thread(() -> processFiles(filesToProcess).subscribe(v -> {
							_LOGGER.debug("Async queue process completed");
						})).start();
						return Mono.just(HttpStatus.ACCEPTED);
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
						var def = new Dem_process_queueDto();
						def.setFile_name(absPath);
						return repoQueue.findById(absPath)
								.defaultIfEmpty(def);
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
	public Flux<SvcUsers> getListOfUsers() throws IxigoException {
		return repoUser.getUsers().map(mapper::fromDtoToSvc);
	}

	private Mono<SvcNodeJsParseOutput> generateMapStatFromFile(File f) throws IxigoException {
		return svcDemProcessor.processDemFile(f).map(stats -> {
			setMapNameAndTime(f, stats);
			return stats;
		});
	}

	/**
	 * It will extract the high level dem file info from the file name
	 * 
	 * @param f
	 * @return
	 */
	private void setMapNameAndTime(File f, SvcNodeJsParseOutput stats) {
		String mapName = Utils.getMapNameFromFile(f, stats.isCs2DemFile());
		LocalDateTime ldt = DateUtils.fromDemFileNameToLocalDateTime(f);
		
		SvcMatchStats ms = new SvcMatchStats();
		ms.setMatch_date(ldt);
		ms.setMapname(cleanMapName(mapName));// TODO is this a corpse?

		stats.getMapStats().setMapname(mapName);
		stats.getMapStats().setMatch_date(ldt);
	}

	private String cleanMapName(String dirtyMapName) {
		if (dirtyMapName.toLowerCase().contains("workshop")) {
			int secondUnderscoreIndex = dirtyMapName.indexOf('_', dirtyMapName.indexOf('_') + 1);
			return dirtyMapName.substring(secondUnderscoreIndex + 1);
		}
		return dirtyMapName;
	}

	private synchronized Mono<Void> processFiles(List<File> files) {
		AtomicInteger countProcessedFiles = new AtomicInteger(0);

		List<ParsingError> errorLists = new ArrayList<>();
		
		LocalDateTime start = LocalDateTime.now();

		// Function to used to extract the data from the DEM file
		Function<? super File, ? extends Publisher<? extends Tuple2<String, SvcNodeJsParseOutput>>> function = f -> {
			return generateMapStatFromFile(f).map(stats -> {
				return Tuples.of(f.getAbsolutePath(), stats);
			}).onErrorResume(error -> {
				_LOGGER.error(String.format("Error while processing %s, msg: %s", f.getAbsolutePath(), error.getMessage()));
				errorLists.add(new ParsingError(f.getAbsolutePath(), error.getMessage()));
				var demStats = new SvcNodeJsParseOutput();
				setMapNameAndTime(f, demStats);
				return Mono.just(Tuples.of(f.getAbsolutePath(), demStats));
			});
		};

		// @formatter:off
		Flux<File> fileFlux = Flux.fromIterable(files);
		Flux<Tuple2<String, SvcNodeJsParseOutput>> flux = null;
		if(props.getProcessFilesInParallel()) {
			_LOGGER.debug("Parsing dem files in parallel");
			flux = fileFlux.parallel()
					.runOn(Schedulers.parallel()) // https://www.vinsguru.com/reactor-schedulers-publishon-vs-subscribeon/
					.flatMap(function)
					.sequential();
		}else {
			_LOGGER.debug("Parsing dem files synchronously");
			flux = fileFlux.flatMap(function);
		}
		
		return flux.map(tuple -> { // Checking if the DEM file had info in it
				String fileName = tuple.getT1();
				SvcNodeJsParseOutput stats = tuple.getT2();
				boolean canProcessTheFile = stats.getAllPlayerStats() != null && !stats.getAllPlayerStats().isEmpty(); 
				return Tuples.of(canProcessTheFile, fileName, stats);
			})
			.map(tuple -> { // Map the DEM file info into appropriate objects
				boolean canProcess = tuple.getT1();
				String fileName = tuple.getT2();
				List<UsersDto> usersList = new ArrayList<UsersDto>();
				SvcNodeJsParseOutput stats = tuple.getT3();
				
				if(canProcess) {
					stats.getAllPlayerStats().forEach(playerScore -> {
						UsersDto user = new UsersDto();
						user.setSteam_id(playerScore.getSteamid());
						user.setUser_name(playerScore.getUsername());
						usersList.add(user);
					});
				}
				
				return Tuples.of(canProcess, fileName, usersList, stats);
			})
			.flatMap(tuple -> { // Saving the objects into the DB
				boolean canProcess = tuple.getT1();
				String fileName = tuple.getT2();
				List<UsersDto> users = tuple.getT3();
				SvcNodeJsParseOutput stats = tuple.getT4(); 
				if(canProcess) {
					
					List<Mono<?>> monos = new ArrayList<>();
					users.forEach(user ->{
						monos.add(repoUser.insertUpdateUser(user));
					});
					
					
					SvcMatchStats mapStats = stats.getMapStats();
					
					Mono<Boolean> monoInsert = this.saveMapStatsAndRetriveId(mapStats)
					.flatMap(id -> {
						Mono<Boolean> allInsert =  this.savePlayersStats(id, stats.getAllPlayerStats())
						.flatMap( b -> this.saveRoundsStats(id, stats.getAllRoundStats()))
						.flatMap( b -> this.savePlayerRoundStats(id, stats.getAllPlayerRoundStats()))
						.flatMap( b -> this.saveRoundKillEvents(id, stats.getAllRoundKillEvents()))
						.flatMap( b -> this.saveRoundShotEvents(id, stats.getAllRoundShotEvents()))
						.flatMap( b -> this.saveRoundHitEvents(id, stats.getAllRoundHitEvents()))
						.flatMap( b -> this.saveRoundEvents(id, stats.getAllRoundEvents()))
						;
						return allInsert;
					});
					monos.add(monoInsert);
										
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
					//f.delete(); TODO should I delete these files?
				}
				return setFileProcessed(f, statusOk ? DemProcessStatus.PROCESSED : DemProcessStatus.DELETED).thenReturn(fileName);
			}).map(fileName ->{ // Simple info
				_LOGGER.debug(String.format("Processed file %s", fileName));
				return fileName;
			})
			.collectList()
			.then(this.triggerFunctions())
			.flatMap(b -> eventSender.sendIxigoEvent(EventType.DEM_FILES_PROCESSED))
			.flatMap(b -> {
				LocalDateTime endTime = LocalDateTime.now(); // End time
		        Duration duration = Duration.between(start, endTime);
		        long days = duration.toDays();
		        long hours = duration.toHoursPart();
		        long minutes = duration.toMinutesPart();
		        long seconds = duration.toSecondsPart();

		        String elapsedTime = String.format("Elapsed Time: %d days, %d hours, %d minutes, %d seconds", days, hours, minutes, seconds);
				return notificationService.sendParsingCompleteNotification(
						"Dem Manager",
						String.format("Processed %d files, %d were successfully processed in %s",
						files.size(),
						countProcessedFiles.get(),
						elapsedTime
					))
					;
			}).map(b -> {
				if(errorLists.isEmpty()) {
					return b;
				}
				String message = errorLists.stream().map(Object::toString)
                .collect(Collectors.joining("\n\n"));
				return notificationService.sendParsingCompleteNotification(
						String.format("%d errors", errorLists.size()),
						message)
						;
			})
			.then();
		// @formatter:on
	}
	
	private Mono<Long> saveMapStatsAndRetriveId(SvcMatchStats mapStats){
		var dtoMs = this.mapper.fromSvcToDto(mapStats);
		
		List<String> whereClause = new ArrayList<>();
		whereClause.add(Match_statsDto.Fields.match_filename);
		
		List<Object> sqlParam = new ArrayList<>();
		sqlParam.add(dtoMs.getMatch_filename());
		return genericRepo.insertAndSelect(Match_statsDao.class, dtoMs, whereClause, sqlParam)
			.map(Match_statsDto::getMatch_id);
	}
	
	private Mono<Boolean> savePlayersStats(Long matchId, List<SvcPlayerStats> list) {
		if(list.isEmpty()) {
			return Mono.just(true);
		}
		List<Mono<Boolean>> monos = new ArrayList<>();
		for (SvcPlayerStats element : list) {
			var dto = this.mapper.fromSvcToDto(element);
			dto.setMatch_id(matchId);
			monos.add(genericRepo.insert(Player_statsDao.class, dto));
		}
		
		Mono<Boolean> mono = monos.get(0);
		for(int i = 1; i < monos.size(); i++) {
			mono = mono.then(monos.get(i));
		}
		return mono;
	}
	
	private Mono<Boolean> saveRoundsStats(Long matchId, List<SvcRoundStats> list) {
		if(list.isEmpty()) {
			return Mono.just(true);
		}
		List<Mono<Boolean>> monos = new ArrayList<>();
		for (SvcRoundStats element : list) {
			var dto = this.mapper.fromSvcToDto(element);
			dto.setMatch_id(matchId);
			monos.add(genericRepo.insert(Round_statsDao.class, dto));
		}
		
		Mono<Boolean> mono = monos.get(0);
		for(int i = 1; i < monos.size(); i++) {
			mono = mono.then(monos.get(i));
		}
		return mono;
	}
	
	private Mono<Boolean> savePlayerRoundStats(Long matchId, List<SvcPlayerRoundStats> list) {
		if(list.isEmpty()) {
			return Mono.just(true);
		}
		List<Mono<Boolean>> monos = new ArrayList<>();
		for (SvcPlayerRoundStats element : list) {
			var dto = this.mapper.fromSvcToDto(element);
			dto.setMatch_id(matchId);
			monos.add(genericRepo.insert(Player_round_statsDao.class, dto));
		}
		
		Mono<Boolean> mono = monos.get(0);
		for(int i = 1; i < monos.size(); i++) {
			mono = mono.then(monos.get(i));
		}
		return mono;
	}
	
	private Mono<Boolean> saveRoundKillEvents(Long matchId, List<SvcRoundKillEvents> list) {
		if(list.isEmpty()) {
			return Mono.just(true);
		}
		List<Mono<Boolean>> monos = new ArrayList<>();
		for (SvcRoundKillEvents element : list) {
			var dto = this.mapper.fromSvcToDto(element);
			dto.setMatch_id(matchId);
			monos.add(genericRepo.insert(Round_kill_eventsDao.class, dto));
		}
		
		Mono<Boolean> mono = monos.get(0);
		for(int i = 1; i < monos.size(); i++) {
			mono = mono.then(monos.get(i));
		}
		return mono;
	}
	
	private Mono<Boolean> saveRoundShotEvents(Long matchId, List<SvcRoundShotEvents> list) {
		if(list.isEmpty()) {
			return Mono.just(true);
		}
		List<Mono<Boolean>> monos = new ArrayList<>();
		for (SvcRoundShotEvents element : list) {
			var dto = this.mapper.fromSvcToDto(element);
			dto.setMatch_id(matchId);
			monos.add(genericRepo.insert(Round_shot_eventsDao.class, dto));
		}
		
		Mono<Boolean> mono = monos.get(0);
		for(int i = 1; i < monos.size(); i++) {
			mono = mono.then(monos.get(i));
		}
		return mono;
	}
	
	private Mono<Boolean> saveRoundHitEvents(Long matchId, List<SvcRoundHitEvents> list) {
		if(list.isEmpty()) {
			return Mono.just(true);
		}
		List<Mono<Boolean>> monos = new ArrayList<>();
		for (SvcRoundHitEvents element : list) {
			var dto = this.mapper.fromSvcToDto(element);
			dto.setMatch_id(matchId);
			monos.add(genericRepo.insert(Round_hit_eventsDao.class, dto));
		}
		
		Mono<Boolean> mono = monos.get(0);
		for(int i = 1; i < monos.size(); i++) {
			mono = mono.then(monos.get(i));
		}
		return mono;
	}
	
	private Mono<Boolean> saveRoundEvents(Long matchId, List<SvcRoundEvents> list) {
		if(list.isEmpty()) {
			return Mono.just(true);
		}
		List<Mono<Boolean>> monos = new ArrayList<>();
		for (SvcRoundEvents element : list) {
			var dto = this.mapper.fromSvcToDto(element);
			dto.setMatch_id(matchId);
			monos.add(genericRepo.insert(Round_eventsDao.class, dto));
		}
		
		Mono<Boolean> mono = monos.get(0);
		for(int i = 1; i < monos.size(); i++) {
			mono = mono.then(monos.get(i));
		}
		return mono;
	}

	private Mono<Boolean> setFileProcessed(File f, DemProcessStatus status) {
		return repoQueue.findById(f.getAbsolutePath()).flatMap(dto -> {
			dto.setProcess_status(status);
			dto.setProcessed_on(DateUtils.getCurrentUtcDateTime());
			return repoQueue.insertOrUpdate(dto);
		});
	}
	
	private Mono<Boolean> triggerFunctions(){
		return this.genericRepo.triggerAllFunctions();
	}

	class ParsingError {
		String fileName;
		String error;

		public ParsingError(String fileName, String error) {
			super();
			this.fileName = fileName;
			this.error = error;
		}

		@Override
		public String toString() {
			return "ParsingError [fileName=" + fileName + ", error=" + error + "]";
		}

	}

	@Override
	public Flux<SvcUserStatsForLastXGames> getUsersStatsForLastXGames(Integer numberOfMatches, List<String> usersIDs) throws IxigoException {
		// @formatter:off
		
		return Flux.fromIterable(usersIDs)
		.parallel()
		.runOn(Schedulers.parallel())
		.map(steamId -> {
			var list = repoUserScore.getLastXMatchesScoresForUser(numberOfMatches, steamId)
					.map(mapper::fromDtoToSvc)
					.collectList()
					;
			return Tuples.of(steamId, list);
		})
		.flatMap(data -> {
			String steamId = data.getT1();
			Mono<List<SvcPlayerMatchStatsExtended>> monoUserScores = data.getT2();
			
			return monoUserScores.map(list -> {
				var stats = new SvcUserStatsForLastXGames();
				stats.setSteamId(steamId);
				stats.setScores(list);
				return stats;
			});
		})
		.sequential()
		;
		// @formatter:on
	}
}
