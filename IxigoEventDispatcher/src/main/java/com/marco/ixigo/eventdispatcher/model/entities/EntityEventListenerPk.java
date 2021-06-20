package com.marco.ixigo.eventdispatcher.model.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.marco.ixigo.eventdispatcher.enums.EventType;

@Embeddable
public class EntityEventListenerPk implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column(name = "URL_LISTENER")
    private String urlListener;
    @Column(name = "EVENT_TYPE")
    @Enumerated(EnumType.STRING)
    private EventType eventType;

    public EntityEventListenerPk() {

    }

    public EntityEventListenerPk(String urlListener, EventType eventType) {
        super();
        this.urlListener = urlListener;
        this.eventType = eventType;
    }

    public String getUrlListener() {
        return urlListener;
    }

    public void setUrlListener(String urlListener) {
        this.urlListener = urlListener;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((eventType == null) ? 0 : eventType.hashCode());
        result = prime * result + ((urlListener == null) ? 0 : urlListener.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        EntityEventListenerPk other = (EntityEventListenerPk) obj;
        if (eventType != other.eventType)
            return false;
        if (urlListener == null) {
            if (other.urlListener != null)
                return false;
        } else if (!urlListener.equals(other.urlListener))
            return false;
        return true;
    }

}
