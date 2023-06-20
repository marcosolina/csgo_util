package com.ixigo.notification.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "ixigo.telegram")
public class TelegramProperties {
	private String token;
	private String chatGroupId;
	private boolean enabled;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getChatGroupId() {
		return chatGroupId;
	}

	public void setChatGroupId(String chatGroupId) {
		this.chatGroupId = chatGroupId;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

}
