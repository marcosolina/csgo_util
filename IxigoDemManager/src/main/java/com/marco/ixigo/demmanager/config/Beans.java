package com.marco.ixigo.demmanager.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import com.marco.ixigo.demmanager.enums.ParserEnvironment;
import com.marco.ixigo.demmanager.repositories.implementations.RepoProcessQueuePostgres;
import com.marco.ixigo.demmanager.repositories.implementations.RepoUserPostgres;
import com.marco.ixigo.demmanager.repositories.implementations.RepoUserScorePostgres;
import com.marco.ixigo.demmanager.repositories.interfaces.RepoProcessQueue;
import com.marco.ixigo.demmanager.repositories.interfaces.RepoUser;
import com.marco.ixigo.demmanager.repositories.interfaces.RepoUserScore;
import com.marco.ixigo.demmanager.services.implementations.DemFileManagerMarco;
import com.marco.ixigo.demmanager.services.implementations.DemFileParserMarco;
import com.marco.ixigo.demmanager.services.implementations.TelegramNotificationService;
import com.marco.ixigo.demmanager.services.implementations.demprocessor.DemProcessorRasp;
import com.marco.ixigo.demmanager.services.implementations.demprocessor.DemProcessorWindows;
import com.marco.ixigo.demmanager.services.interfaces.DemFileManager;
import com.marco.ixigo.demmanager.services.interfaces.DemFileParser;
import com.marco.ixigo.demmanager.services.interfaces.DemProcessor;
import com.marco.ixigo.demmanager.services.interfaces.NotificationService;
import com.marco.utils.network.MarcoNetworkUtils;
import com.marco.utils.network.MarcoNetworkUtilsWebFlux;

@Configuration
public class Beans {

    @Value("${com.marco.ixigo.demmanager.demparser.environment}")
    private ParserEnvironment parserEnv;

    @Bean
    public RepoProcessQueue getRepoProcessQueue() {
        return new RepoProcessQueuePostgres();
    }

    @Bean
    public RepoUser getRepoUser() {
        return new RepoUserPostgres();
    }

    @Bean
    public RepoUserScore getRepoUserScore() {
        return new RepoUserScorePostgres();
    }

    @Bean
    public DemFileManager getDemFileManager() {
        return new DemFileManagerMarco();
    }

    @Bean
    public DemFileParser getDemFileParser() {
        return new DemFileParserMarco();
    }

    @Bean
    public NotificationService getNotificationService() {
        return new TelegramNotificationService();
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
    public DemProcessor getDemProcessor() {
        switch (parserEnv) {
        case RASP:
            return new DemProcessorRasp();
        default:
            return new DemProcessorWindows();
        }
    }
}
