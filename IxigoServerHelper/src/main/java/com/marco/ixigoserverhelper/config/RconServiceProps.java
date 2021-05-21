package com.marco.ixigoserverhelper.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Properties used to call the RCON service
 * 
 * @author Marco
 *
 */
@Configuration
@ConfigurationProperties(prefix = "ixigohelper.rconservice")
public class RconServiceProps {
    private String protocol;
    private String host;
    private int port;
    private String eventEndpoint;

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

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getEventEndpoint() {
        return eventEndpoint;
    }

    public void setEventEndpoint(String eventEndpoint) {
        this.eventEndpoint = eventEndpoint;
    }

}
