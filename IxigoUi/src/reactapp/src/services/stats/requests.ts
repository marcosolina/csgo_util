import {
  IGetStatsRequest,
  IMatchResults,
  IPlayerClutchStats,
  IPlayerOverallStats,
  IPlayerStats,
  ISteamUser,
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
export const PLAYERS_STATS__REQUEST: IGetStatsRequest<IPlayerStats> = {
  viewName: "PLAYER_OVERALL_STATS_EXTENDED_EXTENDED_CACHE",
};

/**
 * It returns the list of Match stats.
 */
export const MATCH_RESULTS_REQUEST: IGetStatsRequest<IMatchResults> = {
  viewName: "MATCH_RESULTS_CACHE",
};

export const PLAYER_OVERALL_STATS_REQUEST: IGetStatsRequest<IPlayerOverallStats> = {
  viewName: "PLAYER_OVERALL_STATS_EXTENDED_EXTENDED_CACHE",
};

export const PLAYER_CLUTCH_STATS_REQUEST: IGetStatsRequest<IPlayerClutchStats> = {
  viewName: "PLAYER_CLUTCH_STATS_CACHE",
};
