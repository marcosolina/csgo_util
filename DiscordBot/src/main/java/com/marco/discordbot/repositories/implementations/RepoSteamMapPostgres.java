package com.marco.discordbot.repositories.implementations;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import com.marco.discordbot.model.entities.EntitySteamMap;
import com.marco.discordbot.repositories.interfaces.RepoSteamMap;

@Transactional
public class RepoSteamMapPostgres implements RepoSteamMap{
    @PersistenceContext
    private EntityManager em;
    
    @Override
    public boolean persist(EntitySteamMap entity) {
        em.merge(entity);
        return true;
    }

    @Override
    public EntitySteamMap findById(Long discordId) {
        return em.find(EntitySteamMap.class, discordId);
    }

    @Override
    public List<EntitySteamMap> getAll() {
        
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<EntitySteamMap> cq = cb.createQuery(EntitySteamMap.class);
        Root<EntitySteamMap> root = cq.from(EntitySteamMap.class);
        /*
         * SELECT * FROM
         */
        cq.select(root);
        
        return em.createQuery(cq).getResultList();
    }

}
