package com.marco.ixigo.ui.config;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "com.marco.ixigo.ui")
public class Urls {
    private Map<String, Map<String, String>> urls;

    public Map<String, Map<String, String>> getUrls() {
        return urls;
    }

    public void setUrls(Map<String, Map<String, String>> urls) {
        this.urls = urls;
    }

}