package com.ixigo.eventdispatcher.models.rest;

import java.io.Serializable;

import com.ixigo.eventdispatcher.enums.EventType;

import io.swagger.annotations.ApiModelProperty;

/**
 * This model represents the information that every listeners should provide
 * upon registration / un-registration
 * 
 * @author Marco
 *
 */
public class EventListenerResgistraionRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(notes = "Where to POST the CSGO event info it is triggered", required = true)
    private String url;
    @ApiModelProperty(notes = "The type of event that you want to register for", required = true)
    private EventType eventType;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

}
