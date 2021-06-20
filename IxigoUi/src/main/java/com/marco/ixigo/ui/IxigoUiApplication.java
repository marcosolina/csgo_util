package com.marco.ixigo.ui;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class IxigoUiApplication {

	public static void main(String[] args) {
		SpringApplication.run(IxigoUiApplication.class, args);
	}

}
