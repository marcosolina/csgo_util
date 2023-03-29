package com.ixigo.discordbot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import com.ixigo.discordbot.services.interfaces.IxigoBot;

@SpringBootApplication
@EnableDiscoveryClient
public class IxigoDiscordBotApplication {
	private static final Logger _LOGGER = LoggerFactory.getLogger(IxigoDiscordBotApplication.class);
	
	public static void main(String[] args) {
		SpringApplication.run(IxigoDiscordBotApplication.class, args);
	}
	
	@Autowired
    private IxigoBot bot;

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                    bot.start().subscribe(status -> _LOGGER.info(String.format("Bot start status: %s", status.toString())));
            }
        };
    }
}
