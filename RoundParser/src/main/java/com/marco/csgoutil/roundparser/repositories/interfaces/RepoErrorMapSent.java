package com.marco.csgoutil.roundparser.repositories.interfaces;

import java.time.LocalDateTime;

import com.marco.csgoutil.roundparser.model.entities.EntityErrorMapSent;

/**
 * Repository used to perform CRUD operation on the ERROR_MAP_SENT table
 * 
 * @author Marco
 *
 */
public interface RepoErrorMapSent {
    /**
     * It inserts / update the entity in the DB
     * 
     * @param entity
     */
    public void insertUpdateUser(EntityErrorMapSent entity);

    /**
     * It returns the entity associated to the input ID, null if not founded
     * 
     * @param gameDate
     * @return
     */
    public EntityErrorMapSent findById(LocalDateTime gameDate);
}
