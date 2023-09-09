import {
  IGetStatsRequest,
  IMatchResults,
  IPlayerClutchStats,
  IPlayerOverallStats,
  ISteamUser,
  IPlayerEntryKillStats,
  ITeamMatchResults,
  IMatchRound,
  IKillCount,
  IRoundKillEvent,
  IRoundEvent,
  IWeaponData,
  IWeaponMatchData,
  IPlayerMatch,
  IWeaponMapData,
} from "./interfaces";

/**
 * It returns the list of Steam users.
 */
export const USERS_REQUEST: IGetStatsRequest<ISteamUser> = {
  viewName: "users",
};

/**
 * It returns the list of players stats.
 */
export const PLAYERS_STATS__REQUEST: IGetStatsRequest<IPlayerMatch> = {
  viewName: "PLAYER_OVERALL_STATS_EXTENDED_EXTENDED",
};

/**
 * It returns the list of Match stats.
 */
export const MATCH_RESULTS_REQUEST: IGetStatsRequest<IMatchResults> = {
  viewName: "MATCH_RESULTS",
};

export const MATCH_TEAM_RESULT_REQUEST: IGetStatsRequest<ITeamMatchResults> = {
  viewName: "PLAYER_MATCH_STATS_EXTENDED",
};

export const PLAYER_MATCH_KILL_COUNT: IGetStatsRequest<IKillCount> = {
  viewName: "PLAYER_MATCH_KILL_COUNT",
};

export const PLAYER_KILL_COUNT: IGetStatsRequest<IKillCount> = {
  viewName: "PLAYER_KILL_COUNT",
};

export const ROUND_SCORECARD_REQUEST: IGetStatsRequest<IMatchRound> = {
  viewName: "ROUND_SCORECARD",
};

export const ROUND_KILL_EVENTS_REQUEST: IGetStatsRequest<IRoundKillEvent> = {
  viewName: "ROUND_KILL_EVENTS",
};

export const ROUND_EVENTS_REQUEST: IGetStatsRequest<IRoundEvent> = {
  viewName: "ROUND_EVENTS",
};

export const PLAYER_OVERALL_STATS_REQUEST: IGetStatsRequest<IPlayerOverallStats> = {
  viewName: "PLAYER_OVERALL_STATS_EXTENDED_EXTENDED",
};

export const PLAYER_CLUTCH_STATS_REQUEST: IGetStatsRequest<IPlayerClutchStats> = {
  viewName: "PLAYER_CLUTCH_STATS",
};

export const PLAYER_ENTRY_KILL_STATS_REQUEST: IGetStatsRequest<IPlayerEntryKillStats> = {
  viewName: "ENTRY_KILL_STATS_EXTENDED",
};

export const OVERALL_PLAYER_WEAPON_STATS_REQUEST: IGetStatsRequest<IWeaponData> = {
  viewName: "OVERALL_PLAYER_WEAPON_STATS",
};

export const MATCH_PLAYER_WEAPON_STATS: IGetStatsRequest<IWeaponMatchData> = {
  viewName: "MATCH_PLAYER_WEAPON_STATS",
};

export const PLAYER_MAP_STATS_EXTENDED_EXTENDED: IGetStatsRequest<IPlayerMatch> = {
  viewName: "PLAYER_MAP_STATS_EXTENDED_EXTENDED",
};

export const MAP_PLAYER_WEAPON_STATS: IGetStatsRequest<IWeaponMapData> = {
  viewName: "MAP_PLAYER_WEAPON_STATS",
};
