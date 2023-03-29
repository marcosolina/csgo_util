package com.ixigo.serverhelper.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "ixigo.end-points.event-dispatcher")
public class EventDispatcherEndPoints {
	private String postEvent;

	public String getPostEvent() {
		return postEvent;
	}

	public void setPostEvent(String postEvent) {
		this.postEvent = postEvent;
	}

}
