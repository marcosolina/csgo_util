package com.ixigo.playersmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class IxigoPlayersManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(IxigoPlayersManagerApplication.class, args);
	}

}
