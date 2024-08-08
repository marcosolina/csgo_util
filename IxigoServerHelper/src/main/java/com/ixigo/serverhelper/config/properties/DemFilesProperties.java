package com.ixigo.serverhelper.config.properties;

import java.nio.file.Path;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "com.ixigo.serverhelper.dem-files")
public class DemFilesProperties {
	private Path demFilesFolderFullPath;
	private Boolean uploadFilesOnlyIfMonday;
	private Boolean deleteFileAfterUpload;

	public Path getDemFilesFolderFullPath() {
		return demFilesFolderFullPath;
	}

	public void setDemFilesFolderFullPath(Path demFilesFolderFullPath) {
		this.demFilesFolderFullPath = demFilesFolderFullPath;
	}

	public Boolean getUploadFilesOnlyIfMonday() {
		return uploadFilesOnlyIfMonday;
	}

	public void setUploadFilesOnlyIfMonday(Boolean uploadFilesOnlyIfMonday) {
		this.uploadFilesOnlyIfMonday = uploadFilesOnlyIfMonday;
	}

	public Boolean getDeleteFileAfterUpload() {
		return deleteFileAfterUpload;
	}

	public void setDeleteFileAfterUpload(Boolean deleteFileAfterUpload) {
		this.deleteFileAfterUpload = deleteFileAfterUpload;
	}
}
