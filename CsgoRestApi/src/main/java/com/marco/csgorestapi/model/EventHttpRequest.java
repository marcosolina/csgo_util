package com.marco.csgorestapi.model;

public class EventHttpRequest {

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
