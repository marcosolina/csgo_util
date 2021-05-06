package com.marco.discordbot.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.reactive.function.client.WebClient;

import com.marco.discordbot.repositories.implementations.RepoSteamMapPostgres;
import com.marco.discordbot.repositories.interfaces.RepoSteamMap;
import com.marco.discordbot.services.implementations.IxiGoBotMarco;
import com.marco.discordbot.services.interfaces.IxiGoBot;

@Configuration
public class Beans {

    @Bean
    public IxiGoBot getIxiGoBot() {
        return new IxiGoBotMarco();
    }

    @Bean
    public RepoSteamMap getRepoSteamMap() {
        return new RepoSteamMapPostgres();
    }

    @Bean
    public WebClient.Builder getWebClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames("classpath:/messages/errorCodes");

        return messageSource;
    }
}
