@feature1 @demFilesManager @demfiles
Feature: DEM files manager
  
  The DEM manger enpoints allows me to upload and retrieve the
  DEM files recorded by the GOTV server

  @addDemFile
  Scenario: Adding a new DEM file
    Given I have a new DEM file
    Then I POST the file to the service
    And I should receive a 201 status in the response

  @getDemFile
  Scenario: Getting a DEM file
    When I perform a GET with the filename
    And I should receive a 200 status in the response
    And I should have the file in the payload

  @getAllDemFiles
  Scenario: Getting all DEM files
    When That I perform a GET to the dem files manager
    Then I should receive a 200 status in the response
    And I should have multiple files in the payload

  @removeFileFromQueue
  Scenario: Remove the DEM file from the queue
    When I perform a DELETE to the dem files manager
    Then I should receive a 200 status in the response
    And I perform another DELETE to the dem files manager
    Then I should receive a 404 status in the response
