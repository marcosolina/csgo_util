package com.marco.ixigo.demmanager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.marco.ixigo.demmanager.repositories.implementations.RepoProcessQueuePostgres;
import com.marco.ixigo.demmanager.repositories.implementations.RepoUserPostgres;
import com.marco.ixigo.demmanager.repositories.implementations.RepoUserScorePostgres;
import com.marco.ixigo.demmanager.repositories.interfaces.RepoProcessQueue;
import com.marco.ixigo.demmanager.repositories.interfaces.RepoUser;
import com.marco.ixigo.demmanager.repositories.interfaces.RepoUserScore;

@Configuration
public class Beans {

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
}
