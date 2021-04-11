package com.marco.csgoutil.roundparser.repositories.interfaces;

import java.math.BigDecimal;
import java.util.List;

import com.marco.csgoutil.roundparser.model.entities.DaoGames;
import com.marco.csgoutil.roundparser.model.entities.EntityUserScore;

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
	public void insertUpdateUserScore(EntityUserScore us);

	/**
	 * It will return a list of the played games/maps (List of LocalDateTime)
	 * 
	 * @return
	 */
	public List<DaoGames> listAvailableGames();

	/**
	 * It returns all the available scores for the specific user
	 * 
	 * @param steamID
	 * @return
	 */
	public List<EntityUserScore> getUserScores(String steamID);

	/**
	 * It returns the most "counter" (input param) recent scores Entity available for the selected
	 * user
	 * 
	 * @param counter
	 * @param steamID
	 * @return
	 */
	public List<EntityUserScore> getLastXUserScores(Integer counter, String steamID, BigDecimal minPercPLayer);

	
	/**
	 * It returns the most "counter" recent scores (Long values) available for the selected user
	 * @param counter
	 * @param steamID
	 * @return
	public List<BigDecimal> getLastXUserScoresValue(Integer counter, String steamID);
	 */
}
