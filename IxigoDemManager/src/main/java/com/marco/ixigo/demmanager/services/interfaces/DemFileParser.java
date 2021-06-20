package com.marco.ixigo.demmanager.services.interfaces;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.marco.ixigo.demmanager.model.dto.MapStats;
import com.marco.ixigo.demmanager.model.rest.demdata.MapPlayed;
import com.marco.ixigo.demmanager.model.rest.demdata.User;
import com.marco.utils.MarcoException;

public interface DemFileParser {
    /**
     * In checks in the queue if there are new files to be processed
     * 
     * @return
     * @throws MarcoException
     */
    public boolean processFiles() throws MarcoException;
    
    public boolean processAllFiles() throws MarcoException;
    
    /**
     * It returns a map of available scores
     * 
     * @return
     */
    public Map<String, String> mapOfAvailableScores();

    /**
     * It returns the number of time we played the different maps
     * 
     * @return
     */
    public List<MapPlayed> countGamesOnAMap();

    /**
     * It will return a list of all the known users
     * 
     * @return
     * @throws MarcoException
     */
    public List<User> getListOfUsers() throws MarcoException;
    
    /**
     * It will return all the Users scores for the most recent "gamesCounter" games
     * 
     * @param gamesCounter
     * @param usersIDs
     * @return
     * @throws MarcoException
     */
    public Map<String, List<MapStats>> getUsersStatsForLastXGames(Integer gamesCounter, List<String> usersIDs,
            BigDecimal minPercPlayed) throws MarcoException;
}
