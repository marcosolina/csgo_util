package com.marco.discordbot.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "demparser.rest")
public class DemParserProperties {
    private String protocol;
    private String host;
    private String getSteamUsers;

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

}
