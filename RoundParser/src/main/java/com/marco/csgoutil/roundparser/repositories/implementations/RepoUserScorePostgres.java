package com.marco.csgoutil.roundparser.repositories.implementations;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.marco.csgoutil.roundparser.model.entities.DaoGames;
import com.marco.csgoutil.roundparser.model.entities.EntityUserScore;
import com.marco.csgoutil.roundparser.model.entities.EntityUserScorePk_;
import com.marco.csgoutil.roundparser.model.entities.EntityUserScore_;
import com.marco.csgoutil.roundparser.repositories.interfaces.RepoUserScore;

@Transactional
public class RepoUserScorePostgres implements RepoUserScore {
	private static final Logger _LOGGER = LoggerFactory.getLogger(RepoUserScorePostgres.class);

	@PersistenceContext
	private EntityManager em;

	@Override
	public void insertUpdateUserScore(EntityUserScore us) {
		_LOGGER.trace("Inside RepoUserScorePostgres.insertUpdateUserScore");
		em.merge(us);
	}

	@Override
	public List<DaoGames> listAvailableGames() {
		_LOGGER.trace("Inside RepoUserScorePostgres.listAvailableGames");
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<DaoGames> cq = cb.createQuery(DaoGames.class);
		Root<EntityUserScore> root = cq.from(EntityUserScore.class);
		
		// @formatter:off
		cq.multiselect(root.get(EntityUserScore_.ID).get(EntityUserScorePk_.GAME_DATE))
			.groupBy(root.get(EntityUserScore_.ID).get(EntityUserScorePk_.GAME_DATE))
			.orderBy(cb.asc(root.get(EntityUserScore_.ID).get(EntityUserScorePk_.GAME_DATE)));
		// @formatter:on
		
		return em.createQuery(cq).getResultList();
	}

	@Override
	public List<EntityUserScore> getUserScores(String steamID) {
		_LOGGER.trace("Inside RepoUserScorePostgres.getUserScores");
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<EntityUserScore> cq = cb.createQuery(EntityUserScore.class);
		Root<EntityUserScore> root = cq.from(EntityUserScore.class);
		
		// @formatter:off
		cq.select(root)
			.where(cb.equal(root.get(EntityUserScore_.ID).get(EntityUserScorePk_.STEAM_ID), steamID))
			.orderBy(cb.asc(root.get(EntityUserScore_.ID).get(EntityUserScorePk_.GAME_DATE)));
		// @formatter:on
		
		return em.createQuery(cq).getResultList();
	}

}
