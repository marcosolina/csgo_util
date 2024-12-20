package com.ixigo.rconapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
// to scan for components defined in the Library project
@ComponentScan(basePackages = "com.ixigo")
public class IxigoRconApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(IxigoRconApiApplication.class, args);
	}

}
