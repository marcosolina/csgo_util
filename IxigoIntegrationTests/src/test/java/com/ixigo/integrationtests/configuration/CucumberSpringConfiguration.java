package com.ixigo.integrationtests.configuration;

import org.springframework.boot.test.context.SpringBootTest;

import io.cucumber.spring.CucumberContextConfiguration;

@CucumberContextConfiguration
@SpringBootTest(classes = { BeansConfig.class }) 
public class CucumberSpringConfiguration {

}
