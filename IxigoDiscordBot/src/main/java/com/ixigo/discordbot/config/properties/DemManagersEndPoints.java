package com.ixigo.discordbot.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "ixigo.end-points.dem-manager")
public class DemManagersEndPoints {
	private String postDemFile;
	private String getDemFile;
	private String getAllDemFiles;
	private String deleteDemFileFromQueue;
	private String postParseQueuedFiles;
	private String postParseAllFiles;
	
	private String getDemDataScoresType; 
	private String getDemDataUsers;
	private String getDemDataUsersScores;
	
	private String getChartsDataMapsPlayed;

	public String getGetDemDataScoresType() {
		return getDemDataScoresType;
	}

	public void setGetDemDataScoresType(String getDemDataScoresType) {
		this.getDemDataScoresType = getDemDataScoresType;
	}

	public String getGetDemDataUsers() {
		return getDemDataUsers;
	}

	public String getGetChartsDataMapsPlayed() {
		return getChartsDataMapsPlayed;
	}

	public void setGetChartsDataMapsPlayed(String getChartsDataMapsPlayed) {
		this.getChartsDataMapsPlayed = getChartsDataMapsPlayed;
	}

	public void setGetDemDataUsers(String getDemDataUsers) {
		this.getDemDataUsers = getDemDataUsers;
	}

	public String getGetDemDataUsersScores() {
		return getDemDataUsersScores;
	}

	public void setGetDemDataUsersScores(String getDemDataUsersScores) {
		this.getDemDataUsersScores = getDemDataUsersScores;
	}

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

	public String getGetAllDemFiles() {
		return getAllDemFiles;
	}

	public void setGetAllDemFiles(String getAllDemFiles) {
		this.getAllDemFiles = getAllDemFiles;
	}

	public String getDeleteDemFileFromQueue(String fileName) {
		return String.format(deleteDemFileFromQueue, fileName);
	}

	public void setDeleteDemFileFromQueue(String deleteDemFileFromQueue) {
		this.deleteDemFileFromQueue = deleteDemFileFromQueue;
	}

	public String getPostParseQueuedFiles() {
		return postParseQueuedFiles;
	}

	public void setPostParseQueuedFiles(String postParseQueuedFiles) {
		this.postParseQueuedFiles = postParseQueuedFiles;
	}

	public String getPostParseAllFiles() {
		return postParseAllFiles;
	}

	public void setPostParseAllFiles(String postParseAllFiles) {
		this.postParseAllFiles = postParseAllFiles;
	}
}
