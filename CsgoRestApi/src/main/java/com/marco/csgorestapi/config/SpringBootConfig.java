package com.marco.csgorestapi.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.reactive.function.client.WebClient;

import com.marco.csgorestapi.repositories.implementations.RepoEntityEventListenerPostgres;
import com.marco.csgorestapi.repositories.interfaces.RepoEntityEventListener;
import com.marco.csgorestapi.services.implementations.EventServiceMarco;
import com.marco.csgorestapi.services.implementations.RconServiceSteamCondenser;
import com.marco.csgorestapi.services.implementations.TelegramNotificationService;
import com.marco.csgorestapi.services.interfaces.EventService;
import com.marco.csgorestapi.services.interfaces.NotificationService;
import com.marco.csgorestapi.services.interfaces.RconService;

/**
 * Standard Spring Boot configuration file
 * 
 * @author Marco
 *
 */
@Configuration
public class SpringBootConfig {

    @Bean
    public RconService getRconService() {
        return new RconServiceSteamCondenser();
    }

    @Bean
    public EventService getEventService() {
        return new EventServiceMarco();
    }

    @Bean
    public RepoEntityEventListener getRepoEntityEventListener() {
        return new RepoEntityEventListenerPostgres();
    }

    @Bean
    public WebClient.Builder getWebClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    public NotificationService getNotificationService() {
        return new TelegramNotificationService();
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames("classpath:/messages/errorCodes");

        return messageSource;
    }
}
