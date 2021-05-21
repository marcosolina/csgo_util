package com.marco.ixigoserverhelper.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Properties used when copying the DEM files
 * 
 * @author Marco
 *
 */
@Configuration
@ConfigurationProperties(prefix = "ixigohelper.demfilesserice")
public class DemFilesServiceProps {
    private String demFilesFolderFullPath;

    public String getDemFilesFolderFullPath() {
        return demFilesFolderFullPath;
    }

    public void setDemFilesFolderFullPath(String demFilesFolderFullPath) {
        this.demFilesFolderFullPath = demFilesFolderFullPath;
    }

}
