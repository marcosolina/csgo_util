package com.ixigo.enums;

public enum BotConfigKey {
	// @formatter:off
    /**
     * Integer - The number of round to consider when splitting the
     * players in 2 teams
     */
    MATCHES_TO_CONSIDER_FOR_TEAM_CREATION,
    /**
     * Boolean - Turn on/off the team autobalance
     */
    AUTOBALANCE,
    
    /**
     * Boolean - Move the Discord users into the specific team voice channel
     */
    MOVE_TO_VOICE_CHANNEL,
    
    /**
     * Boolean - Turn on/off the kick bots
     */
    KICK_BOTS
    // @formatter:on
}