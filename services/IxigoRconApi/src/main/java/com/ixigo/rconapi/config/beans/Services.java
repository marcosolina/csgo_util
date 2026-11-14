package com.ixigo.rconapi.config.beans;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ixigo.rconapi.services.implementations.RconServiceSteamCondenser;
import com.ixigo.rconapi.services.interfaces.RconService;

@Configuration
public class Services {
	@Bean
	public RconService getRconService() {
		return new RconServiceSteamCondenser();
	}
}
