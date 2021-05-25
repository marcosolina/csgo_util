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
    
    /*
    @Autowired
    private IxiGoBot bot;
    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                bot.start();
            }
        };
    }
    */

}
