package com.ixigo.integrationtests.configuration.beans;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ixigo.integrationtests.components.MarcoComponent;

@Configuration
public class CommonBeans {
	@Bean
	public MarcoComponent getMarcoBean() {
		return new MarcoComponent();
	}
}
