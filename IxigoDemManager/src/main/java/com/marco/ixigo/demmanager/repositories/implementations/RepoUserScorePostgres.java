package com.marco.ixigo.demmanager.repositories.implementations;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.marco.ixigo.demmanager.model.entities.DaoGames;
import com.marco.ixigo.demmanager.model.entities.DaoMapGamesCounter;
import com.marco.ixigo.demmanager.model.entities.EntityUserScore;
import com.marco.ixigo.demmanager.model.entities.EntityUserScorePk_;
import com.marco.ixigo.demmanager.model.entities.EntityUserScore_;
import com.marco.ixigo.demmanager.repositories.interfaces.RepoUserScore;

@Transactional
public class RepoUserScorePostgres implements RepoUserScore {
    private static final Logger _LOGGER = LoggerFactory.getLogger(RepoUserScorePostgres.class);

    @PersistenceContext
    private EntityManager em;

    @Override
    public void insertUpdateUserScore(EntityUserScore us) {
        _LOGGER.trace("Inside RepoUserScorePostgres.insertUpdateUserScore");
        /*
         * INSERT INTO USERS_SCORES ...
         */
        em.merge(us);
    }

    @Override
    public List<DaoGames> listAvailableGames() {
        _LOGGER.trace("Inside RepoUserScorePostgres.listAvailableGames");
        
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<DaoGames> cq = cb.createQuery(DaoGames.class);
        Root<EntityUserScore> root = cq.from(EntityUserScore.class);
        
        /*
         * SELECT GAME_DATE FROM USERS_SCORES GROUP BY GAME_DATE ORDER BY GAME_DATE 
         */
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
        
        /*
         * SELECT * FROM USERS_SCORES WHERE STEAM_ID = 'xxx' ORDER BY GAME_DATE
         */
        // @formatter:off
        cq.select(root)
            .where(cb.equal(root.get(EntityUserScore_.ID).get(EntityUserScorePk_.STEAM_ID), steamID))
            .orderBy(cb.asc(root.get(EntityUserScore_.ID).get(EntityUserScorePk_.GAME_DATE)));
        // @formatter:on
        
        return em.createQuery(cq).getResultList();
    }

    @Override
    public List<EntityUserScore> getLastXUserScores(Integer counter, String steamID, BigDecimal minPercPlayed) {
        _LOGGER.trace("Inside RepoUserScorePostgres.getLastXUserScores");
        
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<EntityUserScore> cq = cb.createQuery(EntityUserScore.class);
        Root<EntityUserScore> root = cq.from(EntityUserScore.class);
        
        /*
         * SELECT * FROM USERS_SCORES WHERE STEAM_ID = 'xxx' and MD >= y ORDER BY GAME_DATE DESC LIMIT y
         */
        // @formatter:off
        cq.select(root)
            .where(
                cb.equal(root.get(EntityUserScore_.ID).get(EntityUserScorePk_.STEAM_ID), steamID),
                cb.greaterThanOrEqualTo(root.get(EntityUserScore_.MATCH_PLAYED), minPercPlayed)
            )
            .orderBy(cb.desc(root.get(EntityUserScore_.ID).get(EntityUserScorePk_.GAME_DATE)));
        // @formatter:on
        
        return em.createQuery(cq).setMaxResults(counter).getResultList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<DaoMapGamesCounter> getMapsPlayed() {
        _LOGGER.trace("Inside RepoUserScorePostgres.getMapsPlayed");
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT a.map, COUNT(*)");
        sb.append(" FROM ");
        sb.append(" (");
        sb.append("     SELECT game_date, map");
        sb.append("     FROM");
        sb.append("     users_scores");
        sb.append("     GROUP BY");
        sb.append("     game_date, map");
        sb.append(" ) as a");
        sb.append(" GROUP BY");
        sb.append(" map ");
        sb.append(" order by map");

        Query query = em.createNativeQuery(sb.toString());
        
        List<Object[]> result = query.getResultList();
        
        return result.stream().map(obj -> 
            new DaoMapGamesCounter(String.class.cast(obj[0]), BigInteger.class.cast(obj[1]).longValue())
            ).collect(Collectors.toList());
    }

    
    /*
    @Override
    public List<BigDecimal> getLastXUserScoresValue(Integer counter, String steamID) {
        _LOGGER.trace("Inside RepoUserScorePostgres.getAvgLastXUserScores");
        
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<BigDecimal> cq = cb.createQuery(BigDecimal.class);
        Root<EntityUserScore> root = cq.from(EntityUserScore.class);
        
         * SELECT RWS FROM USERS_SCORES WHERE STEAM_ID = 'xxx' ORDER BY GAME_DATE DESC LIMIT y
        // @formatter:off
        cq.select(root.get(EntityUserScore_.ROUND_WIN_SHARE))
            .where(cb.equal(root.get(EntityUserScore_.ID).get(EntityUserScorePk_.STEAM_ID), steamID))
            .orderBy(cb.desc(root.get(EntityUserScore_.ID).get(EntityUserScorePk_.GAME_DATE)));
        // @formatter:on
        
        return em.createQuery(cq).setMaxResults(counter).getResultList();
    }

     */
}
