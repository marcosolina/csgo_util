package com.ixigo.eventdispatcher.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * End points to send notifications
 * 
 * @author marco
 *
 */
@Configuration
@ConfigurationProperties(prefix = "ixigo.end-points.notification")
public class NotificationEndpoints {
	private String postTelegram;

	public String getPostTelegram() {
		return postTelegram;
	}

	public void setPostTelegram(String postTelegram) {
		this.postTelegram = postTelegram;
	}

}
