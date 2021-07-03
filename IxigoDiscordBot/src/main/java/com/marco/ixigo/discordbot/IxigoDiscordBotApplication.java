package com.marco.ixigo.discordbot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import com.marco.ixigo.discordbot.services.interfaces.IxiGoBot;

@SpringBootApplication
@EnableDiscoveryClient
public class IxigoDiscordBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(IxigoDiscordBotApplication.class, args);
    }

    @Autowired
    private IxiGoBot bot;
    @Value("${com.marco.ixigo.discordbot.startBotOnStartup}")
    private boolean startBotOnStartup;

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                if (startBotOnStartup) {
                    bot.start();
                }
            }
        };
    }
}
