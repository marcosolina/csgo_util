package com.ixigo.eventdispatcher.commands.events;

import com.ixigo.library.mediators.web.interfaces.WebCommandRequest;

public class PostEventCmd implements WebCommandRequest<Void> {
	private String csgoServerEventType;
	private String csgoServerIpAddress;

	public PostEventCmd(String csgoServerEventType, String csgoServerIpAddress) {
		super();
		this.csgoServerEventType = csgoServerEventType;
		this.csgoServerIpAddress = csgoServerIpAddress;
	}

	public String getCsgoServerEventType() {
		return csgoServerEventType;
	}

	public void setCsgoServerEventType(String csgoServerEventType) {
		this.csgoServerEventType = csgoServerEventType;
	}

	public String getCsgoServerIpAddress() {
		return csgoServerIpAddress;
	}

	public void setCsgoServerIpAddress(String csgoServerIpAddress) {
		this.csgoServerIpAddress = csgoServerIpAddress;
	}

}
