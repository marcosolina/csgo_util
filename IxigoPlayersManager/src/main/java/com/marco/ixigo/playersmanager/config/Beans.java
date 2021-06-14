package com.marco.ixigo.playersmanager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.marco.ixigo.playersmanager.services.implementations.PlayersManagerMarco;
import com.marco.ixigo.playersmanager.services.interfaces.PlayersManager;

@Configuration
public class Beans {
    @Bean
    public PlayersManager getPlayersManager() {
        return new PlayersManagerMarco();
    }
}
