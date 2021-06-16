package com.marco.ixigo.discordbot.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.reactive.function.client.WebClient;

import com.marco.ixigo.discordbot.repositories.implementations.RepoEntityBotConfigPostgres;
import com.marco.ixigo.discordbot.repositories.implementations.RepoUsersMapPostgres;
import com.marco.ixigo.discordbot.repositories.interfaces.RepoEntityBotConfig;
import com.marco.ixigo.discordbot.repositories.interfaces.RepoUsersMap;
import com.marco.ixigo.discordbot.services.implementations.IxiGoBotMarco;
import com.marco.ixigo.discordbot.services.implementations.IxiGoGameServerMarco;
import com.marco.ixigo.discordbot.services.interfaces.IxiGoBot;
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
    
    @Bean
    public IxiGoBot getIxiGoBot() {
        return new IxiGoBotMarco();
    }
    
    @Bean(name = "NetworkUtilsNotBalanced")
    public MarcoNetworkUtils getMarcoNetworkUtilsNotBalanced() {
        return new MarcoNetworkUtilsWebFlux(getWebClientBuilderNotBalanced());
    }

    @Bean(name = "WsClientNotBalanced")
    public WebClient.Builder getWebClientBuilderNotBalanced() {
        return WebClient.builder();
    }
    
    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames("classpath:/messages/errorCodes");

        return messageSource;
    }
}
