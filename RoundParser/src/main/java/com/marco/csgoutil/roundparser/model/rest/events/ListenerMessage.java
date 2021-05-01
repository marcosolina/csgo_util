package com.marco.csgoutil.roundparser.model.rest.events;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.marco.csgoutil.roundparser.enums.EventType;

import io.swagger.annotations.ApiModelProperty;

/**
 * Event Message POSTED by the CSGO server
 * 
 * @author Marco
 *
 */
public class ListenerMessage implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(notes = "When the event occur", required = true)
    private LocalDateTime eventTime;
    @ApiModelProperty(notes = "The event type dispatched", required = true)
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

    @Override
    public String toString() {
        return "ListenerMessage [eventTime=" + eventTime + ", eventType=" + eventType + "]";
    }

}
