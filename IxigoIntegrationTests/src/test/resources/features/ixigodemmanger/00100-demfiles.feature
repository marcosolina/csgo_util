@feature1 @demfiles
Feature: DEM files
  
  The DEM manger service extract the match information from the GOTV
  files and stores them into the DB. These inforamtion can be later
  used to balance the teams, generate charts or any other analysis

  @adddemfile
  Scenario: Adding a new DEM file
    Given I have a new DEM file
    Then I POST the file to the service
    # TODO check for 201 instead of 200
    #And I should receive a 201 status in the response
    And I should receive a 200 status in the response

  @getdemfile
  Scenario: Getting a DEM file
    Given That I perform a GET with the filename
    And I should receive a 200 status in the response
    And I should have the file in the payload
