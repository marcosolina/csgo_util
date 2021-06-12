package com.marco.ixigo.eventdispatcher.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.reactive.function.client.WebClient;

import com.marco.ixigo.eventdispatcher.repositories.implementations.RepoEntityEventListenerPostgres;
import com.marco.ixigo.eventdispatcher.repositories.interfaces.RepoEntityEventListener;
import com.marco.ixigo.eventdispatcher.services.implementations.EventServiceMarco;
import com.marco.ixigo.eventdispatcher.services.implementations.TelegramNotificationService;
import com.marco.ixigo.eventdispatcher.services.interfaces.EventService;
import com.marco.ixigo.eventdispatcher.services.interfaces.NotificationService;
import com.marco.utils.network.MarcoNetworkUtils;
import com.marco.utils.network.MarcoNetworkUtilsWebFlux;

public class Beans {
    @Bean(name = "NetworkUtilsNotBalanced")
    public MarcoNetworkUtils getMarcoNetworkUtilsNotBalanced() {
        return new MarcoNetworkUtilsWebFlux(getWebClientBuilderNotBalanced());
    }

    @Bean
    public EventService getEventService() {
        return new EventServiceMarco();
    }

    @Bean
    public RepoEntityEventListener getRepoEntityEventListener() {
        return new RepoEntityEventListenerPostgres();
    }

    @Bean(name = "WsClientNotBalanced")
    public WebClient.Builder getWebClientBuilderNotBalanced() {
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
