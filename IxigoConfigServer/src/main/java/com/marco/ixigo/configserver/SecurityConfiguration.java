package com.marco.ixigo.configserver;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    	http
	    	.csrf()
	        .ignoringAntMatchers("/encrypt/**")
	        .ignoringAntMatchers("/decrypt/**")
	        .and()
	        .authorizeRequests()
	        .anyRequest()
	        .authenticated()
	        .and()
	        .httpBasic()
	        ;
        return http.build();
    }
}