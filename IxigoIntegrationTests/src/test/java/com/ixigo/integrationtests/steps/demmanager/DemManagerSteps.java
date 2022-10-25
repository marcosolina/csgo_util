package com.ixigo.integrationtests.steps.demmanager;

import org.springframework.beans.factory.annotation.Autowired;

import com.ixigo.integrationtests.components.MarcoComponent;

import io.cucumber.java.en.Given;

public class DemManagerSteps {
	@Autowired
	private MarcoComponent component;
	
	@Given("I have the url")
	public void i_have_the_url() {
	    System.out.println(component.hello());
	}
}
