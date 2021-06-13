package com.marco.ixigo.demmanager.repositories.interfaces;

import java.util.List;

import com.marco.ixigo.demmanager.model.entities.EntityProcessQueue;

public interface RepoProcessQueue {
    /**
     * It returns a list of DEM files that are not processed yet
     * 
     * @return
     */
    public List<EntityProcessQueue> getNotProcessedDemFiles();

    /**
     * It stores / updates the entity
     * 
     * @param entity
     * @return
     */
    public boolean saveEntity(EntityProcessQueue entity);
}
