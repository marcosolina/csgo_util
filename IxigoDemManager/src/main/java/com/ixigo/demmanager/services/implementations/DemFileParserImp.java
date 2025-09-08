package com.ixigo.demmanager.services.implementations;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
import com.ixigo.demmanager.models.database.Dem_process_queueDao;
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
import reactor.util.function.Tuple3;
import reactor.util.function.Tuple4;
import reactor.util.function.Tuples;

/**
 * My implementation of the @DemFileParser
 * 
 * @author marco
 *
 */
public class DemFileParserImp implements DemFileParser {
	private static final Logger _LOGGER = LoggerFactory.getLogger(DemFileParserImp.class);

	//TODO toooooooo many dependencies. Refactor this class
	
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
	public Mono<HttpStatus> reprocessFailures() throws IxigoException {
		return this.clearFailedProcessedFiles().flatMap(b -> this.queueAndProcessNewFiles());
	}
	
	@Override
	public Mono<HttpStatus> processQueuedFiles() throws IxigoException {
		_LOGGER.trace("Inside DemFileParserImp.processQueuedFiles");
		// @formatter:off
		
		return repoQueue.getNotProcessedDemFiles()// Get the list of files from the DB
            .collectList()
            .map(this::mapDem_process_queueDtoListToFileList)// Map the repository DTO into a List of files
            .flatMap(this::processFilesBasedOnExectutionTypeConfigured);// Process the files
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
	
	private Mono<HttpStatus> processFilesBasedOnExectutionTypeConfigured(List<File> files){
        /*
         * When testing locally I can configure this to run synchronously 
         * as I will usually process one or two DEM files.
         * When running on the Raspberry PI I will process them
         * synchronously as I will have a lot of DEM files to process. 
         */
        switch (props.getParserExecutionType()) {
        case SYNC:
            //return processFiles(filesToProcess).thenReturn(HttpStatus.ACCEPTED);
            new Thread(() -> processFiles(files).subscribe(v -> {
                _LOGGER.debug("Async queue process completed");
            })).start();
            return Mono.just(HttpStatus.ACCEPTED);
        case ASYNC:
            new Thread(() -> processFiles(files).subscribe(v -> {
                _LOGGER.debug("Async queue process completed");
            })).start();
            return Mono.just(HttpStatus.ACCEPTED);
        default:
            return Mono.just(HttpStatus.ACCEPTED);
        }
    }
    
    private List<File> mapDem_process_queueDtoListToFileList(List<Dem_process_queueDto> dtoList){
        return dtoList.stream().map(e -> new File(e.getFile_name())).collect(Collectors.toList());
    }
    
    private Mono<Boolean> triggerDbFunctions(){
        return this.genericRepo.triggerAllFunctions();
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
    
    private Mono<Boolean> clearFailedProcessedFiles() {
        var dao = new Dem_process_queueDao();
        Map<String,String> whereClause = new HashMap<>();
        whereClause.put(Dem_process_queueDto.Fields.process_status, DemProcessStatus.PROCESS_FAILED.toString());
        _LOGGER.debug("Looking for failed failes");
        
        // TODO don't use generic repo, create a new method in the specific repo.
        return this.genericRepo.getAll(dao.getClass(), Optional.of(whereClause))
            .flatMap(dto -> {
                _LOGGER.debug("Deleting file: " + dto.getFile_name());
                return this.genericRepo.delete(dao.getClass(), dto);
            })
            .collectList()
            .map(l -> l.stream().allMatch(b -> b == true));
    }
    
    private Mono<SvcNodeJsParseOutput> generateMapStatFromFile(File f) throws IxigoException {
        return svcDemProcessor.processDemFile(f).map(stats -> {
            setStatsMapNameAndTime(f, stats);
            return stats;
        });
    }

    /**
     * It will extract the high level dem file info from the file name
     * 
     * @param f
     * @return
     */
    private void setStatsMapNameAndTime(File f, SvcNodeJsParseOutput stats) {
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
    
        
    private Function<? super File, ? extends Publisher<? extends Tuple2<File, SvcNodeJsParseOutput>>> extractDataFromDemFile(List<ParsingError> errorLists) {
        return file -> generateMapStatFromFile(file)
                .map(stats -> Tuples.of(file, stats))
                .onErrorResume(error -> handleParsingError(file, error, errorLists));
    }

    private Mono<Tuple2<File, SvcNodeJsParseOutput>> handleParsingError(File file, Throwable error, List<ParsingError> errorLists) {
        
        String errorMessage = error.getMessage();
        String filePath = file.getAbsolutePath();
        
        _LOGGER.error("Error while processing {}: {}", filePath, filePath);
        
        errorLists.add(new ParsingError(file.getAbsolutePath(), error.getMessage()));

        notificationService.sendParsingCompleteNotification(
                String.format("Error while parsing file: %s", file.getName()),
                errorMessage)
                .subscribe();
        
        var demStats = new SvcNodeJsParseOutput();
        setStatsMapNameAndTime(file, demStats);

        return Mono.just(Tuples.of(file, demStats));
    }

    /**
     * Flag weather the DEM file had stats in it or not
     * @param tuple
     * @return
     */
    private Tuple3<Boolean, File, SvcNodeJsParseOutput> doesDemFileContainStats(Tuple2<File, SvcNodeJsParseOutput> tuple){
        SvcNodeJsParseOutput stats = tuple.getT2();
        boolean canProcessTheFile = stats.getAllPlayerStats() != null && !stats.getAllPlayerStats().isEmpty(); 
        return Tuples.of(canProcessTheFile, tuple.getT1(), stats);
    }
    
    private Tuple4<Boolean, File, List <UsersDto>, SvcNodeJsParseOutput> extractPlayersInfo(Tuple3<Boolean, File, SvcNodeJsParseOutput> fileStats){
        boolean demFileContainsStats = fileStats.getT1();
        var file = fileStats.getT2();
        var stats = fileStats.getT3();
        
        List<UsersDto> usersList = new ArrayList<UsersDto>();
        
        if(demFileContainsStats) {
            stats.getAllPlayerStats().forEach(playerScore -> {
                UsersDto user = new UsersDto();
                user.setSteam_id(playerScore.getSteamid());
                user.setUser_name(playerScore.getUsername());
                usersList.add(user);
            });
        }
        
        return Tuples.of(demFileContainsStats, file, usersList, stats);
    }
    
    private Mono<Tuple2<File, Boolean>> saveStats(Tuple4<Boolean, File, List <UsersDto>, SvcNodeJsParseOutput> fileStats){
        boolean demFileContainsStats = fileStats.getT1();
        var file = fileStats.getT2();
        
        if(!demFileContainsStats) {
            return Mono.just(Tuples.of(file, false));
        }
        
        var users = fileStats.getT3();
        var stats = fileStats.getT4();
        
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
        
        Mono<?> mono = monos.get(0);

        /*
         * Initially I was using the ZIP to run them in parallel, but I had to
         * switch to sequential runs, for some reason it runs out of DB connection.
         * I did not managed to understand how to re-use DB connections.
         * 
         * TODO investigate further
        return Mono.zip(monos, arr -> {
            return true;
        }).thenReturn(Tuples.of(fileName, true));
        */
        for(int i = 1; i < monos.size(); i++) {
            mono = mono.then(monos.get(i));
        }
        
        return mono.thenReturn(Tuples.of(file, true));
    }
    
    private Mono<Tuple2<File, Boolean>> setSuccessfullState(Tuple2<File, Boolean> fileState){
        boolean demFileContainsStats = fileState.getT2();
        var file = fileState.getT1();
        if(!demFileContainsStats) {
            _LOGGER.error(String.format("Process failed: {}", file.getName()));
        }
        
        return setFileProcessed(file, demFileContainsStats ? DemProcessStatus.PROCESSED : DemProcessStatus.PROCESS_FAILED).thenReturn(Tuples.of(file, demFileContainsStats));
    }

    private synchronized Mono<Void> processFiles(List<File> files) {
        AtomicInteger countProcessedFiles = new AtomicInteger(0);

        List<ParsingError> errorLists = new ArrayList<>();
        
        LocalDateTime start = LocalDateTime.now();


        // @formatter:off
        Flux<File> fileFlux = Flux.fromIterable(files);
        Flux<Tuple2<File, SvcNodeJsParseOutput>> flux = null;
        if(props.getProcessFilesInParallel()) {
            _LOGGER.debug("Parsing dem files in parallel");
            flux = fileFlux.parallel()
                    .runOn(Schedulers.parallel()) // https://www.vinsguru.com/reactor-schedulers-publishon-vs-subscribeon/
                    .flatMap(f -> extractDataFromDemFile(errorLists).apply(f))
                    .sequential();
        }else {
            _LOGGER.debug("Parsing dem files synchronously");
            flux = fileFlux.flatMap(f -> extractDataFromDemFile(errorLists).apply(f));
        }
        
        return flux.map(this::doesDemFileContainStats)
            .map(this::extractPlayersInfo)
            .flatMap(this::saveStats)
            .flatMap(this::setSuccessfullState)
            .map(fileState ->
            {   // Just logging some info for debugging
                var fileWasProcessed = fileState.getT2();
                var file = fileState.getT1();
                if(fileWasProcessed) {                  
                    _LOGGER.debug(String.format("Processed file {}", file.getName()));
                    countProcessedFiles.incrementAndGet();
                }
                return fileState;
            })
            .collectList()
            .then(this.triggerDbFunctions())
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
}
