package com.marco.ixigo.playersmanager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import com.marco.ixigo.playersmanager.services.implementations.PlayersManagerMarco;
import com.marco.ixigo.playersmanager.services.implementations.partitionmanager.PartitionTeamsDynamicSearchTree;
import com.marco.ixigo.playersmanager.services.implementations.partitionmanager.PartitionTeamsIxigo;
import com.marco.ixigo.playersmanager.services.interfaces.PartitionTeams;
import com.marco.ixigo.playersmanager.services.interfaces.PlayersManager;
import com.marco.utils.network.MarcoNetworkUtils;
import com.marco.utils.network.MarcoNetworkUtilsWebFlux;

@Configuration
public class Beans {
    @Bean
    public PlayersManager getPlayersManager() {
        return new PlayersManagerMarco();
    }

    @Bean(name = "Simple")
    public PartitionTeams getSimplePartitionTeams() {
        return new PartitionTeamsDynamicSearchTree();
    }

    @Bean(name = "IxiGO")
    public PartitionTeams getIxigoPartitionTeams() {
        return new PartitionTeamsIxigo();
    }
    
    @Bean(name = "NetworkUtilsNotBalanced")
    public MarcoNetworkUtils getMarcoNetworkUtilsNotBalanced() {
        return new MarcoNetworkUtilsWebFlux(getWebClientBuilderNotBalanced());
    }

    @Bean(name = "WsClientNotBalanced")
    public WebClient.Builder getWebClientBuilderNotBalanced() {
        // @formatter:off
        return WebClient.builder().exchangeStrategies(ExchangeStrategies.builder()
                .codecs(configurer -> configurer
                          .defaultCodecs()
                          .maxInMemorySize(50 * 1024 * 1024)) // 50 MB thanks: https://stackoverflow.com/questions/59735951/databufferlimitexception-exceeded-limit-on-max-bytes-to-buffer-webflux-error
                        .build());
        // @formatter:on
    }
}
