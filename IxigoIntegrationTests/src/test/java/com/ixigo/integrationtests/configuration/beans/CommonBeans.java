package com.ixigo.integrationtests.configuration.beans;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ixigo.integrationtests.components.SharedClientResponse;

@Configuration
public class CommonBeans {
	@Bean
	public SharedClientResponse getMarcoBean() {
		return new SharedClientResponse();
	}
}
