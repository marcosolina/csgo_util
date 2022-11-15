package com.ixigo.serverhelper.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "com.ixigo.serverhelper.events")
public class EventProperties {
	private Boolean sendEvents;

	public Boolean getSendEvents() {
		return sendEvents;
	}

	public void setSendEvents(Boolean sendEvents) {
		this.sendEvents = sendEvents;
	}

}
