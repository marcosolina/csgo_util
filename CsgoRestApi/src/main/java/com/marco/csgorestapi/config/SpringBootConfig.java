package com.marco.csgorestapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.marco.csgorestapi.services.implementations.RconServiceSteamCondenser;
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
}
