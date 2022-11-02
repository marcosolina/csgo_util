@feature3 @demFilesData @demfiles
Feature: DEM files data
  
  The DEM files data enpoints returns different info
  about the games we played

  Background: 
    Given that I have two DEM files
    And they are both parsed

  Scenario: Get scores types
    When I perform a GET request to retrieve the type of available scores
    Then I should receive a 200 status in the response
    And I should receive 38 scores type in the response
    