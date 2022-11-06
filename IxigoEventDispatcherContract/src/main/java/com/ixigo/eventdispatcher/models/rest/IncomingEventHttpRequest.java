package com.ixigo.eventdispatcher.models.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This represents the model of the request sent by the CSGO server when an
 * event is fired
 * 
 * @author Marco
 *
 */
public class IncomingEventHttpRequest {
	@JsonProperty("event_name")
    private String eventName;

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    @Override
    public String toString() {
        return "EventHttpRequest [eventName=" + eventName + "]";
    }
}
