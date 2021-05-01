package com.marco.csgorestapi.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import com.marco.csgorestapi.services.implementations.EventServiceMarco;
import com.marco.csgorestapi.services.implementations.RconServiceSteamCondenser;
import com.marco.csgorestapi.services.interfaces.EventService;
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
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasenames("classpath:/messages/errorCodes");

		return messageSource;
	}
}
