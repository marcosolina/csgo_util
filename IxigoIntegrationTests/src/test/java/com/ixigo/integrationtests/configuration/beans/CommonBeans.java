package com.ixigo.integrationtests.configuration.beans;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ixigo.integrationtests.components.SharedResponseEntity;

@Configuration
public class CommonBeans {
	@Bean
	public SharedResponseEntity getMarcoBean() {
		return new SharedResponseEntity();
	}
}
