Feature: 001

  Scenario: Get list
    When the client calls /all
    Then the client receives status code of 200
    And the client receives data
