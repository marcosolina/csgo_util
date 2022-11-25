package com.ixigo.serverhelper.config.properties;

import java.nio.file.Path;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.ixigo.serverhelper.EventFileReaderImplementations;

@Configuration
@ConfigurationProperties(prefix = "com.ixigo.serverhelper.events")
public class EventProperties {
	private Boolean sendEvents;
	private EventFileReaderImplementations eventFileReaderImpl;
	private Path eventFilePath;

	public Boolean getSendEvents() {
		return sendEvents;
	}

	public void setSendEvents(Boolean sendEvents) {
		this.sendEvents = sendEvents;
	}

	public EventFileReaderImplementations getEventFileReaderImpl() {
		return eventFileReaderImpl;
	}

	public void setEventFileReaderImpl(EventFileReaderImplementations eventFileReaderImpl) {
		this.eventFileReaderImpl = eventFileReaderImpl;
	}

	public Path getEventFilePath() {
		return eventFilePath;
	}

	public void setEventFilePath(Path eventFilePath) {
		this.eventFilePath = eventFilePath;
	}

}
