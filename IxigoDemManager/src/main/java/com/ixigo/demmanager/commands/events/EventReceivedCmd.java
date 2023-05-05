package com.ixigo.demmanager.commands.events;

import com.ixigo.eventdispatcher.models.rest.DispatchedEventMessage;
import com.ixigo.library.mediators.web.interfaces.WebCommandRequest;

public class EventReceivedCmd implements WebCommandRequest<Void> {
	private DispatchedEventMessage eventReceived;

	public EventReceivedCmd(DispatchedEventMessage eventReceived) {
		super();
		this.eventReceived = eventReceived;
	}

	public DispatchedEventMessage getEventReceived() {
		return eventReceived;
	}

	public void setEventReceived(DispatchedEventMessage eventReceived) {
		this.eventReceived = eventReceived;
	}

}
