package com.ixigo.integrationtests.cucumberglue;

import org.springframework.beans.factory.annotation.Autowired;

import com.ixigo.integrationtests.MarcoComponent;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class MarcoSteps {
	@Autowired
	private MarcoComponent component;

	@When("^the client calls /all$")
	public void when() {
		System.out.println("When " + component.hello());
	}
	
	@Then("^the client receives status code of 200$")
	public void then() {
		System.out.println("Then " + component.hello());
	}
	
	@And("^the client receives data$")
	public void and() {
		System.out.println("And " + component.hello());
	}
}
