package com.marco.ixigoserverhelper.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.reactive.function.client.WebClient;

import com.marco.ixigoserverhelper.services.implementations.DemFilesServiceMarcoVm;
import com.marco.ixigoserverhelper.services.interfaces.DemFilesService;
import com.marco.utils.network.MarcoNetworkUtils;
import com.marco.utils.network.MarcoNetworkUtilsWebFlux;

/**
 * Standard Spring Config class
 * 
 * @author Marco
 *
 */
@Configuration
public class Beans {
    @Bean
    public DemFilesService getDemFilesService() {
        return new DemFilesServiceMarcoVm();
    }

    /**
     * Client Side load balancing
     * 
     * @return
     */
    @Bean
    @Primary
    @LoadBalanced
    public WebClient.Builder getWebClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    public WebClient.Builder getNonBalancedWebClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    @Primary
    public MarcoNetworkUtils getMarcoNetworkUtils() {
        return new MarcoNetworkUtilsWebFlux(getWebClientBuilder());
    }

    /**
     * When I need to perform an HTTP call to a service which is not registered on
     * my Discovery Server.
     * 
     * @return
     */
    @Bean(name = "NonBalanced")
    public MarcoNetworkUtils getNonBalancedMarcoNetworkUtils() {
        return new MarcoNetworkUtilsWebFlux(getNonBalancedWebClientBuilder());
    }
}
