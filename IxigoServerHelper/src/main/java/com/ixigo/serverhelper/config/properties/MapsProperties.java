package com.ixigo.serverhelper.config.properties;

import java.nio.file.Path;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "com.ixigo.serverhelper.maps")
public class MapsProperties {
	private Path rootFolder;

	public Path getRootFolder() {
		return rootFolder;
	}

	public void setRootFolder(Path rootFolder) {
		this.rootFolder = rootFolder;
	}
}
