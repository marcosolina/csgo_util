package com.ixigo.integrationtests.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.ixigo.integrationtests.components.MarcoComponent;

@ComponentScan
@Configuration
public class BeansConfig {
	@Bean
	public MarcoComponent getMarcoBean() {
		return new MarcoComponent();
	}
}
