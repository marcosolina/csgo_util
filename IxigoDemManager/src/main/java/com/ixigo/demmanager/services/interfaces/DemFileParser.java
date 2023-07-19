package com.ixigo.demmanager.services.interfaces;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;

import com.ixigo.demmanager.models.svc.demdata.data.SvcUser;
import com.ixigo.demmanager.models.svc.demdata.data.SvcUserStatsForLastXGames;
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
    public Mono<HttpStatus> processQueuedFiles() throws IxigoException;
    
    public Mono<HttpStatus> queueAndProcessNewFiles() throws IxigoException;
    
    /**
     * It returns a map of available scores
     * 
     * @return
     */
    public Mono<Map<String, String>> mapOfAvailableScores();

    /**
     * It will return a list of all the known users
     * 
     * @return
     * @throws MarcoException
     */
    public Flux<SvcUser> getListOfUsers() throws IxigoException;
    
    /**
     * It will return all the Users scores for the most recent "numberOfMatches" played
     * with at least "minPercPlayed" of the match
     * 
     * @param gamesCounter
     * @param usersIDs
     * @return
     * @throws MarcoException
     */
    public Flux<SvcUserStatsForLastXGames> getUsersStatsForLastXGames(Integer numberOfMatches, List<String> usersIDs,
            BigDecimal minPercPlayed) throws IxigoException;
}
