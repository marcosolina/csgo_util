package com.ixigo.demmanager.services.interfaces;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;

import com.ixigo.demmanager.models.svc.demdata.SvcMapPlayedCounter;
import com.ixigo.demmanager.models.svc.demdata.SvcUser;
import com.ixigo.demmanager.models.svc.demdata.SvcUserStatsForLastXGames;
import com.ixigo.library.errors.IxigoException;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DemFileParser {
	/**
     * In checks in the queue if there are new files to be processed
     * 
     * @return
     * @throws MarcoException
     */
    public Mono<HttpStatus> processNonProcessedFiles() throws IxigoException;
    
    public Mono<HttpStatus> processAllFiles() throws IxigoException;
    
    /**
     * It returns a map of available scores
     * 
     * @return
     */
    public Mono<Map<String, String>> mapOfAvailableScores();

    /**
     * It returns the number of time we played the different maps
     * 
     * @return
     */
    public Flux<SvcMapPlayedCounter> countGamesOnAMap();

    /**
     * It will return a list of all the known users
     * 
     * @return
     * @throws MarcoException
     */
    public Flux<SvcUser> getListOfUsers() throws IxigoException;
    
    /**
     * It will return all the Users scores for the most recent "gamesCounter" games
     * 
     * @param gamesCounter
     * @param usersIDs
     * @return
     * @throws MarcoException
     */
    public Flux<SvcUserStatsForLastXGames> getUsersStatsForLastXGames(Integer gamesCounter, List<String> usersIDs,
            BigDecimal minPercPlayed) throws IxigoException;
}
