package com.marco.ixigo.playersmanager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import com.marco.ixigo.playersmanager.services.implementations.PlayersManagerMarco;
import com.marco.ixigo.playersmanager.services.interfaces.PlayersManager;
import com.marco.utils.network.MarcoNetworkUtils;
import com.marco.utils.network.MarcoNetworkUtilsWebFlux;

@Configuration
public class Beans {
    @Bean
    public PlayersManager getPlayersManager() {
        return new PlayersManagerMarco();
    }

    @Bean(name = "NetworkUtilsNotBalanced")
    public MarcoNetworkUtils getMarcoNetworkUtilsNotBalanced() {
        return new MarcoNetworkUtilsWebFlux(getWebClientBuilderNotBalanced());
    }

    @Bean(name = "WsClientNotBalanced")
    public WebClient.Builder getWebClientBuilderNotBalanced() {
        return WebClient.builder();
    }
}
