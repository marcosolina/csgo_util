package com.marco.csgorestapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.marco.csgorestapi.services.implementations.MarcoRconService;
import com.marco.csgorestapi.services.interfaces.RconService;

@Configuration
public class SpringBootConfig {

	@Bean
	public RconService getRconService() {
		return new MarcoRconService();
	}
}
