@feature2 @eventDispatcher @events
Feature: Event dispatcher dispatch events
  The event dispatcher service exposes APIs to add
  or remove CSGO event listeners

  @dispatchEvent
  Scenario: Dispatch an event
    When I perform a POST request to dispatch a new event
    Then I should receive a 202 status in the response
