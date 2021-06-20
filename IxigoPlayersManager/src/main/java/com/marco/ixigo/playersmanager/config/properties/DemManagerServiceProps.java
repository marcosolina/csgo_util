package com.marco.ixigo.playersmanager.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "com.marco.ixigo.playersmanager.services.demmanager")
public class DemManagerServiceProps {
    private String protocol;
    private String dns;
    private Integer port;
    private String contextpath;
    private String getUsersScores;
    private String getUsers;

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getDns() {
        return dns;
    }

    public void setDns(String dns) {
        this.dns = dns;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getContextpath() {
        return contextpath;
    }

    public void setContextpath(String contextpath) {
        this.contextpath = contextpath;
    }

    public String getGetUsersScores() {
        return getUsersScores;
    }

    public void setGetUsersScores(String getUsersScores) {
        this.getUsersScores = getUsersScores;
    }

    public String getGetUsers() {
        return getUsers;
    }

    public void setGetUsers(String getUsers) {
        this.getUsers = getUsers;
    }

}
