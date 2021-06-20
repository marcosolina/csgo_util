package com.marco.ixigo.serverhelper.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "com.marco.ixigo.serverhelper")
public class AppProps {
    private String demFilesFolderFullPath;

    public String getDemFilesFolderFullPath() {
        return demFilesFolderFullPath;
    }

    public void setDemFilesFolderFullPath(String demFilesFolderFullPath) {
        this.demFilesFolderFullPath = demFilesFolderFullPath;
    }
}
