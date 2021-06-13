package com.marco.ixigo.demmanager.repositories.implementations;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.marco.ixigo.demmanager.enums.DemProcessStatus;
import com.marco.ixigo.demmanager.model.entities.EntityProcessQueue;
import com.marco.ixigo.demmanager.model.entities.EntityProcessQueue_;
import com.marco.ixigo.demmanager.repositories.interfaces.RepoProcessQueue;

@Transactional
public class RepoProcessQueuePostgres implements RepoProcessQueue {
    private static final Logger _LOGGER = LoggerFactory.getLogger(RepoProcessQueuePostgres.class);

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<EntityProcessQueue> getNotProcessedDemFiles() {
        _LOGGER.trace("Inside RepoProcessQueuePostgres.getNotProcessedDemFiles");
        
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<EntityProcessQueue> cq = cb.createQuery(EntityProcessQueue.class);
        Root<EntityProcessQueue> root = cq.from(EntityProcessQueue.class);
        
        /*
         * SELECT * FROM DEM_PROCESS_QUEUE WHERE PROCESS_STATUS = 'NOT_PROCESSED' ORDER BY QUEUED_ON
         */
        // @formatter:off
        cq.select(root)
            .where(cb.equal(root.get(EntityProcessQueue_.PROCESS_STATUS), DemProcessStatus.NOT_PROCESSED))
            .orderBy(cb.asc(root.get(EntityProcessQueue_.QUEUED_ON)));
        // @formatter:on
        
        return em.createQuery(cq).getResultList();
    }

    @Override
    public boolean saveEntity(EntityProcessQueue entity) {
        _LOGGER.trace("Inside RepoProcessQueuePostgres.saveEntity");
        em.merge(entity);
        return true;
    }

}
