package com.marco.csgorestapi.repositories.implementations;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import com.marco.csgorestapi.enums.EventType;
import com.marco.csgorestapi.model.entities.EntityEventListener;
import com.marco.csgorestapi.model.entities.EntityEventListenerPk;
import com.marco.csgorestapi.model.entities.EntityEventListenerPk_;
import com.marco.csgorestapi.model.entities.EntityEventListener_;
import com.marco.csgorestapi.repositories.interfaces.RepoEntityEventListener;

/**
 * PostgreSQL implementation
 * 
 * @author Marco
 *
 */
@Transactional
public class RepoEntityEventListenerPostgres implements RepoEntityEventListener {
    private static final Logger _LOGGER = LoggerFactory.getLogger(RepoEntityEventListenerPostgres.class);

    @PersistenceContext
    private EntityManager em;

    @Override
    public boolean registerNewListener(EntityEventListener entity) {
        if (_LOGGER.isTraceEnabled()) {
            _LOGGER.trace("Inside RepoEntityEventListenerPostgres:registerNewListener");
        }
        em.persist(entity);
        return true;
    }

    @Override
    public List<EntityEventListener> getListernersOfEvent(EventType event) {
        if (_LOGGER.isTraceEnabled()) {
            _LOGGER.trace("Inside RepoEntityEventListenerPostgres:getListernersOfEvent");
        }

        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<EntityEventListener> cq = cb.createQuery(EntityEventListener.class);
        Root<EntityEventListener> root = cq.from(EntityEventListener.class);

        // @formatter:off
        cq.select(root)
        .where(
                cb.and(
                        cb.lessThan(root.get(EntityEventListener_.CONSECUTIVE_FAILURE), 3),
                        cb.notEqual(root.get(EntityEventListener_.ACTIVE), "N"),
                        cb.equal(root.get(EntityEventListener_.ID).get(EntityEventListenerPk_.EVENT_TYPE), event)
                )
            );
        // @formatter:on

        return em.createQuery(cq).getResultList();
    }

    @Override
    public boolean deleteListener(EntityEventListenerPk key) {
        EntityEventListener entity = em.find(EntityEventListener.class, key);
        if (entity == null) {
            return false;
        }
        em.remove(entity);
        return true;
    }

    @Override
    public EntityEventListener findListener(EntityEventListenerPk key) {
        return em.find(EntityEventListener.class, key);
    }

    @Override
    public boolean updateEntity(EntityEventListener entity) {
        em.merge(entity);
        return true;
    }

}
