package com.ixigo.integrationtests.configuration.cucumber;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import com.ixigo.integrationtests.configuration.beans.DefaultBeans;

import io.cucumber.spring.CucumberContextConfiguration;

@CucumberContextConfiguration
@SpringBootTest(classes = { DefaultBeans.class })
@ContextConfiguration
@EnableConfigurationProperties
public class CucumberSpringConfiguration {

}
