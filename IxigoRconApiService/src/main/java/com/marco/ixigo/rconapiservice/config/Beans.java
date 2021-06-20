package com.marco.ixigo.rconapiservice.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import com.marco.ixigo.rconapiservice.services.implementations.RconServiceSteamCondenser;
import com.marco.ixigo.rconapiservice.services.interfaces.RconService;

@Configuration
public class Beans {
    @Bean
    public RconService getRconService() {
        return new RconServiceSteamCondenser();
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames("classpath:/messages/errorCodes");

        return messageSource;
    }
}
