package com.marco.ixigo.eventdispatcher.repositories.interfaces;

import java.util.List;

import com.marco.ixigo.eventdispatcher.enums.EventType;
import com.marco.ixigo.eventdispatcher.model.entities.EntityEventListener;
import com.marco.ixigo.eventdispatcher.model.entities.EntityEventListenerPk;

public interface RepoEntityEventListener {

    /**
     * It register a new Event Listener
     * 
     * @param entity
     * @return
     */
    public boolean registerNewListener(EntityEventListener entity);

    /**
     * It updates the entity
     * 
     * @param entity
     * @return
     */
    public boolean updateEntity(EntityEventListener entity);

    /**
     * It returns a list of event listeners of the specific event. The list contains
     * only "active" listeners which hare still responding
     * 
     * @param event
     * @return
     */
    public List<EntityEventListener> getListernersOfEvent(EventType event);

    /**
     * Deletes the listener
     * 
     * @param entity
     * @return
     */
    public boolean deleteListener(EntityEventListenerPk key);

    /**
     * It returns the listener definition
     * 
     * @param key
     * @return
     */
    public EntityEventListener findListener(EntityEventListenerPk key);

}
