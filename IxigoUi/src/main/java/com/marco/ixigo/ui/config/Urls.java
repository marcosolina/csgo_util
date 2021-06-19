package com.marco.ixigo.ui.config;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "com.marco.ixigo.ui")
public class Urls {
    private Map<String, Map<String, String>> urls;
}
