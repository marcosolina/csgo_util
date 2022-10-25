package com.ixigo.integrationtests.cucumberglue;

import org.springframework.boot.test.context.SpringBootTest;

import com.ixigo.integrationtests.SpringTestConfig;

import io.cucumber.spring.CucumberContextConfiguration;

@CucumberContextConfiguration
@SpringBootTest(classes = { SpringTestConfig.class }) 
public class CucumberSpringConfiguration {

}
