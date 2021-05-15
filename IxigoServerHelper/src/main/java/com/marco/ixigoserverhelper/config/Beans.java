package com.marco.ixigoserverhelper.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import com.marco.ixigoserverhelper.services.implementations.DemFilesServiceMarcoVm;
import com.marco.ixigoserverhelper.services.interfaces.DemFilesService;
import com.marco.utils.network.MarcoNetworkUtils;
import com.marco.utils.network.MarcoNetworkUtilsWebFlux;

@Configuration
public class Beans {
    @Bean
    public DemFilesService getDemFilesService() {
        return new DemFilesServiceMarcoVm();
    }
    
    @Bean
    @LoadBalanced
    public WebClient.Builder getWebClientBuilder() {
        return WebClient.builder();
    }
    
    @Bean
    public MarcoNetworkUtils getMarcoNetworkUtils() {
        return new MarcoNetworkUtilsWebFlux(getWebClientBuilder());
    }
}
