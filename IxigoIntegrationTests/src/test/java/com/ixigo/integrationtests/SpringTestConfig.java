package com.ixigo.integrationtests;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@ComponentScan
@Configuration
public class SpringTestConfig {
	@Bean
	public MarcoComponent getMarcoBean() {
		return new MarcoComponent();
	}
}
