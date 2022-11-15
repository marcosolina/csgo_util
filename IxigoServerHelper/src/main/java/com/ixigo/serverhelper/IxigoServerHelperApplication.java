package com.ixigo.serverhelper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class IxigoServerHelperApplication {

	public static void main(String[] args) {
		SpringApplication.run(IxigoServerHelperApplication.class, args);
	}

}
