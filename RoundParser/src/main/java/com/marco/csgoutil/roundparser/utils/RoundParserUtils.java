package com.marco.csgoutil.roundparser.utils;

public class RoundParserUtils {
	
	private RoundParserUtils() {}
	
	public static final String MAPPING_ADD_NEW_DATA = "/newdata";
	public static final String MAPPING_GET_USERS = "/users";
	public static final String MAPPING_GET_USER_SCORES = "/user/{steamID}/scores";
	public static final String MAPPING_GET_USER_LAST_SCORES = "/users/last/{counter}/games/scores";
	public static final String MAPPING_GET_USER_AVG_SCORES = "/users/avg/last/{counter}/games/scores";
	public static final String MAPPING_GET_TEAMS = "/{teamsCounter}/using/last/{counter}/games/scores";
}
