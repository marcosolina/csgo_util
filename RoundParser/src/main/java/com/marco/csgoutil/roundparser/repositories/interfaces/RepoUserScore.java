package com.marco.csgoutil.roundparser.repositories.interfaces;

import java.util.List;

import com.marco.csgoutil.roundparser.model.entities.DaoGames;
import com.marco.csgoutil.roundparser.model.entities.EntityUserScore;

public interface RepoUserScore {

	public void insertUpdateUserScore(EntityUserScore us);
	
	public List<DaoGames> listAvailableGames();
	
	public List<EntityUserScore> getUserScores(String steamID);
	
	public List<EntityUserScore> getLastXUserScores(Integer counter, String steamID);
	
	public List<Long> getLastXUserScoresValue(Integer counter, String steamID);
}
