package com.ixigo.ui;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "ixigo")
public class IxigoEndPoints {

	private Map<String, Map<String, String>> endPoints;

	public Map<String, Map<String, String>> getEndPoints() {
		return endPoints;
	}

	public void setEndPoints(Map<String, Map<String, String>> endPoints) {
		this.endPoints = endPoints;
	}

}
