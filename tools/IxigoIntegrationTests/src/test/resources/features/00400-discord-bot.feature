@feature4 @discordBot
Feature: Discord Bot
  The Discord bot service exposes APIs
  to manage the users in the Discord voice chat
  and also send command to the CSGO server using
  the RCON APIs

  @warmupEvent
  Scenario: Warmup Event
    When I dispatch a round_announce_warmup
    Then I should receive a 200 status in the response
