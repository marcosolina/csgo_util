import { IGetStatsRequest, IMatchResults, IPlayerStats, ISteamUser } from "./interfaces";

/**
 * Generic requests for the stats service.
 */

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
 * It returns the list of players stats.
 */
export const MATCH_RESULTS_REQUEST: IGetStatsRequest<IMatchResults> = {
  viewName: "MATCH_RESULTS_CACHE",
};
