package com.ixigo.integrationtests.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "ixigo.end-points.demmanager")
public class DemManagersEndPoints {
	private String postDemFile;
	private String getDemFile;

	public String getPostDemFile() {
		return postDemFile;
	}

	public void setPostDemFile(String postDemFile) {
		this.postDemFile = postDemFile;
	}

	public String getGetDemFile(String fileName) {
		return String.format(getDemFile, fileName);
	}

	public void setGetDemFile(String getDemFile) {
		this.getDemFile = getDemFile;
	}

}
