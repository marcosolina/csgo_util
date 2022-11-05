package com.ixigo.eventdispatcher.config.beans;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ixigo.eventdispatcher.repositories.implementations.RepoEntityEventListenerPostgres;
import com.ixigo.eventdispatcher.repositories.interfaces.RepoEntityEventListener;

@Configuration
public class Repositories {
	@Bean
	public RepoEntityEventListener getRepoEntityEventListener() {
		return new RepoEntityEventListenerPostgres();
	}
}
