package com.ixigo.demmanager.repositories.interfaces;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.ixigo.demmanager.models.database.DtoMapPlayedCounter;
import com.ixigo.demmanager.models.database.Users_scoresDto;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * CRUD operation that you can perform on the score table
 * 
 * @author Marco
 *
 */
public interface RepoUserScore {
	/**
     * Insert/Update the user score entity
     * 
     * @param us
     */
    public Mono<Boolean> insertUpdateUserScore(Users_scoresDto userScore);

    /**
     * It will return a list of the played games/maps (List of LocalDateTime)
     * 
     * @return
     */
    public Flux<LocalDateTime> listAvailableGames();

    /**
     * It returns the number of times we played a specific map
     * 
     * @return
     */
    public Flux<DtoMapPlayedCounter> getMapsPlayed();

    /**
     * It returns all the available scores for the specific user
     * 
     * @param steamID
     * @return
     */
    public Flux<Users_scoresDto> getUserScores(String steamID);

    /**
     * It returns the user scores for the last "numberOfMatches" he has played
     * for at least "minPercPlayed" per match
     * 
     * @param numberOfMatches
     * @param steamID
     * @param minPercPLayer (min 0 - max 1)
     * @return
     */
    public Flux<Users_scoresDto> getLastXMatchesScoresForUser(Integer numberOfMatches, String steamID, BigDecimal minPercPlayed);

    /**
     * It returns the most "counter" recent scores (Long values) available for the
     * selected user
     * 
     * @param counter
     * @param steamID
     * @return public List<BigDecimal> getLastXUserScoresValue(Integer counter,
     *         String steamID);
     */
}
