/*
 * Terminating all the connections
 */

SELECT pg_terminate_backend(pg_stat_activity.pid) FROM pg_stat_activity WHERE pg_stat_activity.datname = 'demfiles';

/*
 * Select the database
 */
\c demfiles;


/*
 * INDEXES
 */

CREATE INDEX idx_match_id_match_stats ON MATCH_STATS(match_id);
CREATE INDEX idx_match_date_match_stats ON MATCH_STATS(match_date);

CREATE INDEX idx_match_id_player_round_stats ON PLAYER_ROUND_STATS(match_id);
CREATE INDEX idx_steam_id_player_round_stats ON PLAYER_ROUND_STATS(steamID);
CREATE INDEX idx_round_player_round_stats ON PLAYER_ROUND_STATS(round);

CREATE INDEX idx_match_id_round_events ON ROUND_EVENTS(match_id);
CREATE INDEX idx_steam_id_round_events ON ROUND_EVENTS(steamID);
CREATE INDEX idx_round_round_events ON ROUND_EVENTS(round);

CREATE INDEX idx_match_id_round_kill_events ON ROUND_KILL_EVENTS(match_id);
CREATE INDEX idx_steam_id_round_kill_events ON ROUND_KILL_EVENTS(steamID);
CREATE INDEX idx_round_round_kill_events ON ROUND_KILL_EVENTS(round);

CREATE INDEX idx_match_id_round_shot_events ON ROUND_SHOT_EVENTS(match_id);
CREATE INDEX idx_steam_id_round_shot_events ON ROUND_SHOT_EVENTS(steamID);
CREATE INDEX idx_round_round_shot_events ON ROUND_SHOT_EVENTS(round);

CREATE INDEX idx_match_id_round_hit_events ON ROUND_HIT_EVENTS(match_id);
CREATE INDEX idx_steam_id_round_hit_events ON ROUND_HIT_EVENTS(steamID);
CREATE INDEX idx_round_round_hit_events ON ROUND_HIT_EVENTS(round);

CREATE INDEX idx_match_id_player_stats ON PLAYER_STATS(match_id);
CREATE INDEX idx_steam_id_player_stats ON PLAYER_STATS(steamID);

CREATE INDEX idx_match_id_round_stats ON ROUND_STATS(match_id);


CREATE INDEX idx_player_round_stats_composite_killer ON PLAYER_ROUND_STATS(steamid, match_id, round);
CREATE INDEX idx_round_events_composite ON ROUND_EVENTS(steamID, match_id, round);
CREATE INDEX idx_round_kill_events_composite ON ROUND_KILL_EVENTS(steamID, match_id, round);
CREATE INDEX idx_round_shot_events_composite ON ROUND_SHOT_EVENTS(steamID, match_id, round);
CREATE INDEX idx_round_hit_events_composite ON ROUND_HIT_EVENTS(steamID, match_id, round);

CREATE INDEX idx_player_round_stats_steamid_matchid ON PLAYER_ROUND_STATS(steamID, match_id);
CREATE INDEX idx_round_events_steamid_matchid ON ROUND_EVENTS(steamID, match_id);
CREATE INDEX idx_round_kill_events_steamid_matchid ON ROUND_KILL_EVENTS(steamID, match_id);
CREATE INDEX idx_round_shot_events_steamid_matchid ON ROUND_SHOT_EVENTS(steamID, match_id);
CREATE INDEX idx_round_hit_events_steamid_matchid ON ROUND_HIT_EVENTS(steamID, match_id);
