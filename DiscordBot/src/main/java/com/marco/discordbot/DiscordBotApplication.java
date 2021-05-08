package com.marco.discordbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class DiscordBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(DiscordBotApplication.class, args);
    }

}
