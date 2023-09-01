package com.ixigo.demmanager.repositories.interfaces;

import com.ixigo.demmanager.models.database.Player_match_stats_extendedDto;

import reactor.core.publisher.Flux;

public interface RepoUserScore {
	/**
	 * It returns the user scores for the last "numberOfMatches" he has played for
	 * at least "minPercPlayed" per match
	 * 
	 * @param numberOfMatches
	 * @param steamID
	 * @param minPercPLayer   (min 0 - max 1)
	 * @return
	 */
	public Flux<Player_match_stats_extendedDto> getLastXMatchesScoresForUser(Integer numberOfMatches, String steamID);
}
