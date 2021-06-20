package com.marco.ixigo.serverhelper.model.events;

/**
 * Rest model used to notify the RCON service that a new IxiGo event occurred on
 * the Game server
 * 
 * @author Marco
 *
 */
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
