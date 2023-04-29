package com.ixigo.demmanager.repositories.interfaces;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.ixigo.demmanager.enums.ScoreType;
import com.ixigo.demmanager.models.database.DtoMapPlayedCounter;
import com.ixigo.demmanager.models.database.DtoTeamAvgScorePerMap;
import com.ixigo.demmanager.models.database.DtoUserAvgScorePerMap;
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
	 * It returns the list of scores per map
	 * 
	 * @param mapName
	 * @return
	 */
	public Flux<Users_scoresDto> getUserScoresPerMap(String mapName, Optional<Integer> lastXMatchedToConsider);

	/**
	 * It returns the average score type of a single user per map
	 * 
	 * @param steamId
	 * @return
	 */
	public Flux<DtoUserAvgScorePerMap> getUserAveragaScorePerMap(String steamId, ScoreType scoreType, Optional<List<String>> maps, Optional<Integer> lastMatchesToConsider);
	
	/**
	 * It returns the Team average score per map
	 * @param mapName
	 * @param scoreType
	 * @param lastMatchesToConsider
	 * @return
	 */
	public Flux<DtoTeamAvgScorePerMap> getAvgTeamScorePerMap(String mapName, ScoreType scoreType, Optional<Integer> lastMatchesToConsider);

	/**
	 * It returns the user scores for the last "numberOfMatches" he has played for
	 * at least "minPercPlayed" per match
	 * 
	 * @param numberOfMatches
	 * @param steamID
	 * @param minPercPLayer   (min 0 - max 1)
	 * @return
	 */
	public Flux<Users_scoresDto> getLastXMatchesScoresForUser(Integer numberOfMatches, String steamID, BigDecimal minPercPlayed);
}
