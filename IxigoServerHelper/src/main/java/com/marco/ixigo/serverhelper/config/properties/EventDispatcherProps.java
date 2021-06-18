package com.marco.ixigo.serverhelper.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "com.marco.ixigo.serverhelper.services.eventdispatcher")
public class EventDispatcherProps {
    private String protocol;
    private String host;
    private Integer port;
    private String dispatchEvent;
    private boolean sendEvents;

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

    public String getDispatchEvent() {
        return dispatchEvent;
    }

    public void setDispatchEvent(String dispatchEvent) {
        this.dispatchEvent = dispatchEvent;
    }

    public boolean isSendEvents() {
        return sendEvents;
    }

    public void setSendEvents(boolean sendEvents) {
        this.sendEvents = sendEvents;
    }

}
