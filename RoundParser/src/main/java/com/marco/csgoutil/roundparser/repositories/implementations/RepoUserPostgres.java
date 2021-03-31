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

import com.marco.csgoutil.roundparser.model.entities.EntityUser;
import com.marco.csgoutil.roundparser.repositories.interfaces.RepoUser;

@Transactional
public class RepoUserPostgres implements RepoUser {
	private static final Logger _LOGGER = LoggerFactory.getLogger(RepoUserPostgres.class);
	
	@PersistenceContext
	private EntityManager em;
	
	@Override
	public List<EntityUser> getUsers() {
		_LOGGER.trace("Inside RepoUserPostgres.getUsers");
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<EntityUser> cq = cb.createQuery(EntityUser.class);
		Root<EntityUser> root = cq.from(EntityUser.class);
		
		/*
		 * SELECT * FROM USERS
		 */
		cq.select(root);
		
		return em.createQuery(cq).getResultList();
	}

	@Override
	public void insertUpdateUser(EntityUser user) {
		_LOGGER.trace("Inside RepoUserPostgres.insertUser");
		/*
		 * INSERT INTO USERS ... 
		 */
		em.merge(user);
	}

	@Override
	public EntityUser findById(String steamID) {
		/*
		 * SELECT * FROM USERS WHERE STEAM_ID = 'xxx'
		 */
		return em.find(EntityUser.class, steamID);
	}
}
