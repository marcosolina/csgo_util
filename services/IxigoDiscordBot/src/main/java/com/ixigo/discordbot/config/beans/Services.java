package com.ixigo.discordbot.config.beans;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ixigo.discordbot.services.implementations.IxigoBotImpl;
import com.ixigo.discordbot.services.implementations.IxigoPlayersManagerServiceImpl;
import com.ixigo.discordbot.services.implementations.IxigoRconServiceImpl;
import com.ixigo.discordbot.services.interfaces.IxigoBot;
import com.ixigo.discordbot.services.interfaces.IxigoPlayersManagerService;
import com.ixigo.discordbot.services.interfaces.IxigoRconService;

@Configuration
public class Services {
	@Bean
	public IxigoBot getIxigoBot() {
		return new IxigoBotImpl();
	}

	@Bean
	public IxigoPlayersManagerService getIxigoPlayersManagerService() {
		return new IxigoPlayersManagerServiceImpl();
	}

	@Bean
	public IxigoRconService getIxigoRconService() {
		return new IxigoRconServiceImpl();
	}
}
