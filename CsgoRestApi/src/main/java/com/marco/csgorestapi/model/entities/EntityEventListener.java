package com.marco.csgorestapi.model.entities;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Table wich will hold the info of the event Listeners that were registered
 * 
 * @author Marco
 *
 */
@Entity
@Table(name = "EVENT_LISTENERS")
public class EntityEventListener {

    @EmbeddedId
    private EntityEventListenerPk id;
    @Column(name = "LAST_SUCCESSFUL")
    private LocalDateTime lastSuccessful;
    @Column(name = "LAST_FAILURE")
    private LocalDateTime lastFailure;
    @Column(name = "CONSECUTIVE_FAILURE")
    private Integer consecutiveFailure;
    @Column(name = "ACTIVE")
    private String active;

    public EntityEventListenerPk getId() {
        return id;
    }

    public void setId(EntityEventListenerPk id) {
        this.id = id;
    }

    public LocalDateTime getLastSuccessful() {
        return lastSuccessful;
    }

    public void setLastSuccessful(LocalDateTime lastSuccessful) {
        this.lastSuccessful = lastSuccessful;
    }

    public LocalDateTime getLastFailure() {
        return lastFailure;
    }

    public void setLastFailure(LocalDateTime lastFailure) {
        this.lastFailure = lastFailure;
    }

    public Integer getConsecutiveFailure() {
        return consecutiveFailure;
    }

    public void setConsecutiveFailure(Integer consecutiveFailure) {
        this.consecutiveFailure = consecutiveFailure;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

}
