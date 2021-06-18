package com.marco.ixigo.serverhelper.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import com.marco.utils.network.MarcoNetworkUtils;
import com.marco.utils.network.MarcoNetworkUtilsWebFlux;

@Configuration
public class Beans {
    @Bean(name = "NetworkUtilsNotBalanced")
    public MarcoNetworkUtils getMarcoNetworkUtilsNotBalanced() {
        return new MarcoNetworkUtilsWebFlux(getWebClientBuilderNotBalanced());
    }

    @Bean(name = "WsClientNotBalanced")
    public WebClient.Builder getWebClientBuilderNotBalanced() {
        return WebClient.builder();
    }
}
