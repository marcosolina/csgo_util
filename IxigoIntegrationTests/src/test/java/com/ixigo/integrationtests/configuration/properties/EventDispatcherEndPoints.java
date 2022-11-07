package com.ixigo.integrationtests.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "ixigo.end-points.eventdispatcher")
public class EventDispatcherEndPoints {
	private String postEvent;
	private String postListener;
	private String deleteListener;

	public String getPostEvent() {
		return postEvent;
	}

	public void setPostEvent(String postEvent) {
		this.postEvent = postEvent;
	}

	public String getPostListener() {
		return postListener;
	}

	public void setPostListener(String postListener) {
		this.postListener = postListener;
	}

	public String getDeleteListener() {
		return deleteListener;
	}

	public void setDeleteListener(String deleteListener) {
		this.deleteListener = deleteListener;
	}

}
