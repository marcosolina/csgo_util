package com.ixigo.serverhelper.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "ixigo.end-points.dem-manager")
public class DemManagersEndPoints {
	private String postDemFile;
	private String postParseQueuedFiles;

	public String getPostDemFile() {
		return postDemFile;
	}

	public void setPostDemFile(String postDemFile) {
		this.postDemFile = postDemFile;
	}

	public String getPostParseQueuedFiles() {
		return postParseQueuedFiles;
	}

	public void setPostParseQueuedFiles(String postParseQueuedFiles) {
		this.postParseQueuedFiles = postParseQueuedFiles;
	}
}
