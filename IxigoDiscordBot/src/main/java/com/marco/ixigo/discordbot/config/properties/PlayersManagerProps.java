package com.marco.ixigo.discordbot.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "com.marco.ixigo.discordbot.service.players-manager")
public class PlayersManagerProps {
    private String protocol;
    private String host;
    private Integer port;
    private String contextPath;
    private String generateTeams;

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

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getContextPath() {
        return contextPath;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    public String getGenerateTeams() {
        return generateTeams;
    }

    public void setGenerateTeams(String generateTeams) {
        this.generateTeams = generateTeams;
    }

}
