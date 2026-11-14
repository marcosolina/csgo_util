@feature3 @demFilesData @demfiles
Feature: DEM files data
  
  The DEM files data enpoints returns different info
  about the games we played

  Background: 
    Given that I have two DEM files
    And they are both parsed
    And I give 20 seconds to the server to process the dem files

  Scenario: Get scores types
    When I perform a GET request to retrieve the type of available scores
    Then I should receive a 200 status in the response
    And I should receive 38 scores type in the response

  Scenario: Get maps played
    When I perform a GET request to retrieve maps played counters
    Then I should receive a 200 status in the response
    And I should receive 2 maps in the response
    And the map list should contain the following maps:
      | de_inferno |
      | de_mirage  |

  Scenario: Get list of steam users
    When I perform a GET request to retrieve the list of users
    Then I should receive a 200 status in the response
    And I should receive 13 users in the response
    And the users list should contain the following names:
      | EIK3000              |
      | Buckshot             |
      | Zigzagatha Christie  |
      | S.S.G. Tolkien       |
      | Wagatha Christie     |
      | Tippeh               |
      | Marco                |
      | chiCkin              |
      | Sir Pollo Loco       |
      | Lagatha Christie     |
      | Handbagatha Christie |
      | dannyhangboy         |
      | vks2020              |

  Scenario: Get users scores for muliple matches
    Given that I want to retrieve the scores for the last 2 matches for the following users:
      | 76561197962351233 |
      | 76561197965589644 |
      | 00000000000000000 |
    Then I perform a GET request to retrieve the users score
    And I should receive a 200 status in the response
    And I should have the following data in the response body
      | steamId           | number_of_matches_founded |
      | 76561197962351233 |                         1 |
      | 76561197965589644 |                         2 |
      | 00000000000000000 |                         0 |
