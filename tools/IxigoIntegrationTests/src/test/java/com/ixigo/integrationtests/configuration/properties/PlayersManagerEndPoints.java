package com.ixigo.integrationtests.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "ixigo.end-points.players-manager")
public class PlayersManagerEndPoints {
	private String getTeams;

	public String getGetTeams() {
		return getTeams;
	}

	public void setGetTeams(String getTeams) {
		this.getTeams = getTeams;
	}
}
