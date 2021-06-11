package com.marco.ixigo.demmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class IxigoDemManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(IxigoDemManagerApplication.class, args);
	}

}
