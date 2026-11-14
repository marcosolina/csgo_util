package com.ixigo.eventdispatcher.commands.listeners;

import com.ixigo.eventdispatcher.enums.EventType;
import com.ixigo.library.mediators.web.interfaces.WebCommandRequest;

public class DeleteListenerCmd implements WebCommandRequest<Void> {
	private EventType listenForEventType;
	private String listenerUrl;

	public DeleteListenerCmd(EventType listenForEventType, String listenerUrl) {
		super();
		this.listenForEventType = listenForEventType;
		this.listenerUrl = listenerUrl;
	}

	public EventType getListenForEventType() {
		return listenForEventType;
	}

	public void setListenForEventType(EventType listenForEventType) {
		this.listenForEventType = listenForEventType;
	}

	public String getListenerUrl() {
		return listenerUrl;
	}

	public void setListenerUrl(String listenerUrl) {
		this.listenerUrl = listenerUrl;
	}

}
