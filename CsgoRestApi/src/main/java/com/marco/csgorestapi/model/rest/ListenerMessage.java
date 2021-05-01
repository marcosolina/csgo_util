package com.marco.csgorestapi.model.rest;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.marco.csgorestapi.enums.EventType;

import io.swagger.annotations.ApiModelProperty;

/**
 * Model sent to the listeners when a CSGO event is fired
 * 
 * @author Marco
 *
 */
public class ListenerMessage implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(notes = "When the event occur", required = true)
    private LocalDateTime eventTime;
    @ApiModelProperty(notes = "The CSGO event type", required = true)
    private EventType eventType;

    public LocalDateTime getEventTime() {
        return eventTime;
    }

    public void setEventTime(LocalDateTime eventTime) {
        this.eventTime = eventTime;
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

}
