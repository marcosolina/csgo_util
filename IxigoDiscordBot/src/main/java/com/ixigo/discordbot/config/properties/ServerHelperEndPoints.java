package com.ixigo.discordbot.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "ixigo.end-points.server-helper")
public class ServerHelperEndPoints {
	private String postCs2input;

	public String getPostCs2input() {
		return postCs2input;
	}

	public void setPostCs2input(String postCs2input) {
		this.postCs2input = postCs2input;
	}
}
