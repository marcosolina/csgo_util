package com.ixigo.ui;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
// to scan for components defined in the Library project
@ComponentScan(basePackages = "com.ixigo")
public class IxigoUiApplication {

	public static void main(String[] args) {
		SpringApplication.run(IxigoUiApplication.class, args);
	}

}
