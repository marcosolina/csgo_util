package com.ixigo.serverhelper.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "com.ixigo.serverhelper.demFiles")
public class DemFilesProperties {
	private String demFilesFolderFullPath;
	private Boolean uploadFilesOnlyIfMonday;

	public String getDemFilesFolderFullPath() {
		return demFilesFolderFullPath;
	}

	public void setDemFilesFolderFullPath(String demFilesFolderFullPath) {
		this.demFilesFolderFullPath = demFilesFolderFullPath;
	}

	public Boolean getUploadFilesOnlyIfMonday() {
		return uploadFilesOnlyIfMonday;
	}

	public void setUploadFilesOnlyIfMonday(Boolean uploadFilesOnlyIfMonday) {
		this.uploadFilesOnlyIfMonday = uploadFilesOnlyIfMonday;
	}

}
