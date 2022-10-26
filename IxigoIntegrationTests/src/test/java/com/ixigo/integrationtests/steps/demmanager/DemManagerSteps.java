package com.ixigo.integrationtests.steps.demmanager;

import org.springframework.beans.factory.annotation.Autowired;

import com.ixigo.integrationtests.components.MarcoComponent;

import io.cucumber.java.en.Given;

public class DemManagerSteps {
	@Autowired
	private MarcoComponent component;
	
	@Given("I have a new DEM file")
	public void i_have_a_new_dem_file() {
	    System.out.println(component.hello());
	}
}
