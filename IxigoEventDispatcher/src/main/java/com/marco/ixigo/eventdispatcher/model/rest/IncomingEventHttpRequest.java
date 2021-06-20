package com.marco.ixigo.eventdispatcher.model.rest;

/**
 * This represents the model of the request sent by the CSGO server when an
 * event is fired
 * 
 * @author Marco
 *
 */
public class IncomingEventHttpRequest {
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
