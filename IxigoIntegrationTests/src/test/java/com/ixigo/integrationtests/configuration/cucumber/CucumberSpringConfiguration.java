package com.ixigo.integrationtests.configuration.cucumber;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;

import com.ixigo.integrationtests.configuration.beans.DefaultBeans;

import io.cucumber.spring.CucumberContextConfiguration;

@CucumberContextConfiguration
@SpringBootTest(classes = { DefaultBeans.class })
@EnableConfigurationProperties
public class CucumberSpringConfiguration {

}
