package com.marco.ixigo.discordbot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "com.marco.ixigo.discordbot.services.rconapi")
public class RconApiProps {
    private String protocol;
    private String host;
    private Integer port;
    private String contextPath;
    private String sendcommand;

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getContextPath() {
        return contextPath;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    public String getSendcommand() {
        return sendcommand;
    }

    public void setSendcommand(String sendcommand) {
        this.sendcommand = sendcommand;
    }

}
