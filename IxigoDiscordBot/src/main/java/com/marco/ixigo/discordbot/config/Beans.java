package com.marco.ixigo.discordbot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import com.marco.ixigo.discordbot.repositories.implementations.RepoEntityBotConfigPostgres;
import com.marco.ixigo.discordbot.repositories.implementations.RepoUsersMapPostgres;
import com.marco.ixigo.discordbot.repositories.interfaces.RepoEntityBotConfig;
import com.marco.ixigo.discordbot.repositories.interfaces.RepoUsersMap;
import com.marco.ixigo.discordbot.services.implementations.IxiGoGameServerMarco;
import com.marco.ixigo.discordbot.services.interfaces.IxiGoGameServer;
import com.marco.utils.network.MarcoNetworkUtils;
import com.marco.utils.network.MarcoNetworkUtilsWebFlux;

@Configuration
public class Beans {

    @Bean
    public IxiGoGameServer getIxiGoGameServer() {
        return new IxiGoGameServerMarco();
    }
    
    @Bean
    public RepoEntityBotConfig getRepoEntityBotConfig() {
        return new RepoEntityBotConfigPostgres();
    }
    
    @Bean
    public RepoUsersMap getRepoUsersMap() {
        return new RepoUsersMapPostgres();
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