package com.ixigo.integrationtests.configuration;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import io.cucumber.core.options.Constants;


// https://stackoverflow.com/questions/64587739/cucumberoptions-in-cucumber-junit-platform-engine
@Suite
@SelectClasspathResource("features")
@ConfigurationParameter(key = Constants.GLUE_PROPERTY_NAME, 
value = "com.ixigo.integrationtests.configuration,"
		+ "com.ixigo.integrationtests.steps.demmanager")
@ConfigurationParameter(key = Constants.PLUGIN_PUBLISH_QUIET_PROPERTY_NAME, value = "true")
public class CucumberConfiguration {

}
