package com.marco.ixigoserverhelper.models.rest.events;

public class SendEvent {
    private String eventName;

    public SendEvent() {
    }

    public SendEvent(String evnName) {
        this.eventName = evnName;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

}
