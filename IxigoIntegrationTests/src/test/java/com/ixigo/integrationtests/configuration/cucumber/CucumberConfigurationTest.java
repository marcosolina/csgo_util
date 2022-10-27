package com.ixigo.integrationtests.configuration.cucumber;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import io.cucumber.core.options.Constants;

// https://stackoverflow.com/questions/64587739/cucumberoptions-in-cucumber-junit-platform-engine
@Suite
@SelectClasspathResource("features")
@ConfigurationParameter(key = Constants.PLUGIN_PUBLISH_QUIET_PROPERTY_NAME, value = "true")

@ConfigurationParameter(key = Constants.GLUE_PROPERTY_NAME,
// @formatter:off
value = "com.ixigo.integrationtests.configuration.cucumber,"
		+ "com.ixigo.integrationtests.steps.demmanager")
// @formatter:on
public class CucumberConfigurationTest {
// Keep the suffic "Test" in the class name otherwise it will not picked up by maven test
}
