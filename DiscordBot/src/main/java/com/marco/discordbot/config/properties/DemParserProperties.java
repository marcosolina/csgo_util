package com.marco.discordbot.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "demparser.rest")
public class DemParserProperties {
    private String protocol;
    private String host;
    private String contextPath;
    private String getSteamUsers;
    private String createTeams;
    private String movePlayers;

    public String getMovePlayers() {
        return movePlayers;
    }

    public void setMovePlayers(String movePlayers) {
        this.movePlayers = movePlayers;
    }

    public String getCreateTeams() {
        return createTeams;
    }

    public void setCreateTeams(String createTeams) {
        this.createTeams = createTeams;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getGetSteamUsers() {
        return getSteamUsers;
    }

    public void setGetSteamUsers(String getSteamUsers) {
        this.getSteamUsers = getSteamUsers;
    }

    public String getContextPath() {
        return contextPath;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

}