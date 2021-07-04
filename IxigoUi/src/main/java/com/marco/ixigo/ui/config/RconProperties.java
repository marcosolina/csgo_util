package com.marco.ixigo.ui.config;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "com.marco.ixigo.ui")
public class RconProperties {
    private Map<String, String> rconapi;

    public Map<String, String> getProps() {
        return rconapi;
    }

    public void setProps(Map<String, String> props) {
        this.rconapi = props;
    }

}
