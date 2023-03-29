@feature2 @demFilesParser @demfiles
Feature: DEM files parser
  
  The DEM files parser enpoints allows me to trigger the
  process which will analyse the DEM file/s and extract
  the statistics of the players

  Background: 
    Given that I have two DEM files
    And only one of them is marked as queued

  Scenario: Parse queued DEM files
    When I perform a POST request to parse the queued files
    And I give 20 seconds to the server to process the dem files
    Then I should receive a 202 status in the response

  Scenario: Parse queued and non queued DEM files
    When I perform a POST request to parse the queued and non queued files
    And I give 20 seconds to the server to process the dem files
    Then I should receive a 202 status in the response    
   
