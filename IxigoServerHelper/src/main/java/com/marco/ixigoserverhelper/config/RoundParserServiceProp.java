package com.marco.ixigoserverhelper.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Properties used to call the DEM Service
 * 
 * @author Marco
 *
 */
@Configuration
@ConfigurationProperties(prefix = "ixigohelper.demfilesserice.parserservice")
public class RoundParserServiceProp {
    private String protocol;
    private String host;
    private String endpoint;

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

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }
}
