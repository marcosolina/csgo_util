package com.marco.ixigoserverhelper.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.marco.ixigoserverhelper.services.implementations.DemFilesServiceMarcoVm;
import com.marco.ixigoserverhelper.services.interfaces.DemFilesService;

@Configuration
public class Beans {
    @Bean
    public DemFilesService getDemFilesService() {
        return new DemFilesServiceMarcoVm();
    }
}
