@feature2 @eventDispatcher @eventListeners
Feature: Event dispatcher manage event listeners
  The event dispatcher service exposes APIs to add
  or remove CSGO event listeners

  @registerEventListener
  Scenario: Registering an event listener
  	Given I dont have the listener registered
    When I perform a POST request to register a new listern
    Then I should receive a 201 status in the response
    
  @registerEventListener
  Scenario: Registering an event listener with wrong data
  	Given I dont have the listener registered
    When I perform a POST request to register a new listern with wrong data
    Then I should receive a 400 status in the response
    
	@registerEventListener
  Scenario: Registering duplicated event listener
  	Given I dont have the listener registered
    When I perform a POST request to register a new listern
    Then I should receive a 201 status in the response
    When I perform a POST request to register again the same listener
    Then I should receive a 409 status in the response
    
	@unregisterEventListener
  Scenario: Un-Registering event listener
  	Given I dont have the listener registered
    When I perform a POST request to register a new listern
    Then I should receive a 201 status in the response
    When I perform a DELETE request to un-register the listener
    Then I should receive a 200 status in the response
