package com.marco.csgoutil.roundparser.repositories.implementations;

import java.time.LocalDateTime;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.marco.csgoutil.roundparser.model.entities.EntityErrorMapSent;
import com.marco.csgoutil.roundparser.repositories.interfaces.RepoErrorMapSent;

@Transactional
public class RepoErrorMapSentPostgres implements RepoErrorMapSent {
    private static final Logger _LOGGER = LoggerFactory.getLogger(RepoErrorMapSentPostgres.class);

    @PersistenceContext
    private EntityManager em;

    @Override
    public void insertUpdateUser(EntityErrorMapSent entity) {
        _LOGGER.trace("Inside RepoErrorMapSentPostgres.insertUpdateUser");
        /*
         * INSERT INTO ERROR_MAP_SENT ... UPDATE ERROR_MAP_SENT ...
         */
        em.merge(entity);

    }

    @Override
    public EntityErrorMapSent findById(LocalDateTime gameDate) {
        /*
         * SELECT * FROM ERROR_MAP_SENT WHERE GAME_DATE = 'xxx'
         */
        return em.find(EntityErrorMapSent.class, gameDate);
    }

}
