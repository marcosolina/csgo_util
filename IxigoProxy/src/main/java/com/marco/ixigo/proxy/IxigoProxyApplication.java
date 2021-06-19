package com.marco.ixigo.proxy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class IxigoProxyApplication {

	public static void main(String[] args) {
		SpringApplication.run(IxigoProxyApplication.class, args);
	}

}
