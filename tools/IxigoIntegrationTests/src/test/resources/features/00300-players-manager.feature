@feature3 @playersManager
Feature: Players manager
  The players manager service exposes APIs
  to generate two balanced CSGO teams

  @generateTeams
  Scenario: Generate teams
    Given that I want to split the following players into two teams
      | steamId           |
      | 76561197961466528 |
      | 76561197965589644 |
      | 76561197974132960 |
      | 76561198072484969 |
      | 76561198093594380 |
      | 76561199045993244 |
      | 76561197963053225 |
    When I perform a GET request to the players manager with the following query parameters
      | numberOfMatches | penaltyWeigth | partitionScore | minPercPlayed |
      |              10 |           0.4 | HLTV           |           0.9 |
    Then I should receive a 200 status in the response
    And the two teams shold have the following specs
      | teamIndex | teamScore |
      |         0 |      4.03 |
      |         1 |      4.12 |
    And the players of the teams should be
      | team_one_players  | team_two_players  |
      | 76561197974132960 | 76561197961466528 |
      | 76561199045993244 | 76561197965589644 |
      | 76561197963053225 | 76561198093594380 |
      | 76561198072484969 |                 0 |
