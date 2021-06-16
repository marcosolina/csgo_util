package com.marco.ixigo.discordbot.repositories.implementations;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import com.marco.ixigo.discordbot.model.entities.EntityUserMap;
import com.marco.ixigo.discordbot.repositories.interfaces.RepoUsersMap;

@Transactional
public class RepoUsersMapPostgres implements RepoUsersMap {
    @PersistenceContext
    private EntityManager em;

    @Override
    public boolean persist(EntityUserMap entity) {
        em.merge(entity);
        return true;
    }

    @Override
    public EntityUserMap findById(Long discordId) {
        return em.find(EntityUserMap.class, discordId);
    }

    @Override
    public List<EntityUserMap> getAll() {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<EntityUserMap> cq = cb.createQuery(EntityUserMap.class);
        Root<EntityUserMap> root = cq.from(EntityUserMap.class);
        /*
         * SELECT * FROM
         */
        cq.select(root);

        return em.createQuery(cq).getResultList();
    }

}
