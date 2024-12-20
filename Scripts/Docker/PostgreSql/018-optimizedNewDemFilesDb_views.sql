\c demfiles;

CREATE TABLE ROUND_KILL_EVENTS_EXTENDED_CACHE AS SELECT * FROM ROUND_KILL_EVENTS_EXTENDED;

CREATE OR REPLACE FUNCTION refresh_ROUND_KILL_EVENTS_EXTENDED_CACHE() RETURNS void AS
$$
BEGIN
    TRUNCATE ROUND_KILL_EVENTS_EXTENDED_CACHE;
    INSERT INTO ROUND_KILL_EVENTS_EXTENDED_CACHE SELECT * FROM ROUND_KILL_EVENTS_EXTENDED;
END
$$
LANGUAGE 'plpgsql';
    

--INSERT INTO cron.job (schedule, command, nodename, nodeport, database, username)
--VALUES ('0 0 * * *', $$CALL refresh_ROUND_KILL_EVENTS_EXTENDED_CACHE()$$, 'localhost', 5432, 'DEMFILES', 'REPLACE_ME');
    
CREATE TABLE PLAYER_KILL_COUNT_CACHE AS SELECT * FROM PLAYER_KILL_COUNT;

CREATE OR REPLACE FUNCTION refresh_PLAYER_KILL_COUNT_CACHE() RETURNS void AS
$$
BEGIN
    TRUNCATE PLAYER_KILL_COUNT_CACHE;
    INSERT INTO PLAYER_KILL_COUNT_CACHE SELECT * FROM PLAYER_KILL_COUNT;
END
$$
LANGUAGE 'plpgsql';
    

--INSERT INTO cron.job (schedule, command, nodename, nodeport, database, username)
--VALUES ('0 0 * * *', $$CALL refresh_PLAYER_KILL_COUNT_CACHE()$$, 'localhost', 5432, 'DEMFILES', 'REPLACE_ME');
    
CREATE TABLE PLAYER_MATCH_KILL_COUNT_CACHE AS SELECT * FROM PLAYER_MATCH_KILL_COUNT;

CREATE OR REPLACE FUNCTION refresh_PLAYER_MATCH_KILL_COUNT_CACHE() RETURNS void AS
$$
BEGIN
    TRUNCATE PLAYER_MATCH_KILL_COUNT_CACHE;
    INSERT INTO PLAYER_MATCH_KILL_COUNT_CACHE SELECT * FROM PLAYER_MATCH_KILL_COUNT;
END
$$
LANGUAGE 'plpgsql';
    

--INSERT INTO cron.job (schedule, command, nodename, nodeport, database, username)
--VALUES ('0 0 * * *', $$CALL refresh_PLAYER_MATCH_KILL_COUNT_CACHE()$$, 'localhost', 5432, 'DEMFILES', 'REPLACE_ME');
    
CREATE TABLE PLAYER_WEAPON_MAP_KILLS_CACHE AS SELECT * FROM PLAYER_WEAPON_MAP_KILLS;

CREATE OR REPLACE FUNCTION refresh_PLAYER_WEAPON_MAP_KILLS_CACHE() RETURNS void AS
$$
BEGIN
    TRUNCATE PLAYER_WEAPON_MAP_KILLS_CACHE;
    INSERT INTO PLAYER_WEAPON_MAP_KILLS_CACHE SELECT * FROM PLAYER_WEAPON_MAP_KILLS;
END
$$
LANGUAGE 'plpgsql';
    

--INSERT INTO cron.job (schedule, command, nodename, nodeport, database, username)
--VALUES ('0 0 * * *', $$CALL refresh_PLAYER_WEAPON_MAP_KILLS_CACHE()$$, 'localhost', 5432, 'DEMFILES', 'REPLACE_ME');
    
CREATE TABLE PLAYER_WEAPON_MATCH_KILLS_CACHE AS SELECT * FROM PLAYER_WEAPON_MATCH_KILLS;

CREATE OR REPLACE FUNCTION refresh_PLAYER_WEAPON_MATCH_KILLS_CACHE() RETURNS void AS
$$
BEGIN
    TRUNCATE PLAYER_WEAPON_MATCH_KILLS_CACHE;
    INSERT INTO PLAYER_WEAPON_MATCH_KILLS_CACHE SELECT * FROM PLAYER_WEAPON_MATCH_KILLS;
END
$$
LANGUAGE 'plpgsql';
    

--INSERT INTO cron.job (schedule, command, nodename, nodeport, database, username)
--VALUES ('0 0 * * *', $$CALL refresh_PLAYER_WEAPON_MATCH_KILLS_CACHE()$$, 'localhost', 5432, 'DEMFILES', 'REPLACE_ME');
    
CREATE TABLE PLAYER_WEAPON_OVERALL_KILLS_CACHE AS SELECT * FROM PLAYER_WEAPON_OVERALL_KILLS;

CREATE OR REPLACE FUNCTION refresh_PLAYER_WEAPON_OVERALL_KILLS_CACHE() RETURNS void AS
$$
BEGIN
    TRUNCATE PLAYER_WEAPON_OVERALL_KILLS_CACHE;
    INSERT INTO PLAYER_WEAPON_OVERALL_KILLS_CACHE SELECT * FROM PLAYER_WEAPON_OVERALL_KILLS;
END
$$
LANGUAGE 'plpgsql';
    

--INSERT INTO cron.job (schedule, command, nodename, nodeport, database, username)
--VALUES ('0 0 * * *', $$CALL refresh_PLAYER_WEAPON_OVERALL_KILLS_CACHE()$$, 'localhost', 5432, 'DEMFILES', 'REPLACE_ME');
    
CREATE TABLE ROUND_HIT_EVENTS_EXTENDED_CACHE AS SELECT * FROM ROUND_HIT_EVENTS_EXTENDED;

CREATE OR REPLACE FUNCTION refresh_ROUND_HIT_EVENTS_EXTENDED_CACHE() RETURNS void AS
$$
BEGIN
    TRUNCATE ROUND_HIT_EVENTS_EXTENDED_CACHE;
    INSERT INTO ROUND_HIT_EVENTS_EXTENDED_CACHE SELECT * FROM ROUND_HIT_EVENTS_EXTENDED;
END
$$
LANGUAGE 'plpgsql';
    

--INSERT INTO cron.job (schedule, command, nodename, nodeport, database, username)
--VALUES ('0 0 * * *', $$CALL refresh_ROUND_HIT_EVENTS_EXTENDED_CACHE()$$, 'localhost', 5432, 'DEMFILES', 'REPLACE_ME');
    
CREATE TABLE ENTRY_KILL_STATS_CACHE AS SELECT * FROM ENTRY_KILL_STATS;

CREATE OR REPLACE FUNCTION refresh_ENTRY_KILL_STATS_CACHE() RETURNS void AS
$$
BEGIN
    TRUNCATE ENTRY_KILL_STATS_CACHE;
    INSERT INTO ENTRY_KILL_STATS_CACHE SELECT * FROM ENTRY_KILL_STATS;
END
$$
LANGUAGE 'plpgsql';
    

--INSERT INTO cron.job (schedule, command, nodename, nodeport, database, username)
--VALUES ('0 0 * * *', $$CALL refresh_ENTRY_KILL_STATS_CACHE()$$, 'localhost', 5432, 'DEMFILES', 'REPLACE_ME');
    
CREATE TABLE ENTRY_KILL_STATS_EXTENDED_CACHE AS SELECT * FROM ENTRY_KILL_STATS_EXTENDED;

CREATE OR REPLACE FUNCTION refresh_ENTRY_KILL_STATS_EXTENDED_CACHE() RETURNS void AS
$$
BEGIN
    TRUNCATE ENTRY_KILL_STATS_EXTENDED_CACHE;
    INSERT INTO ENTRY_KILL_STATS_EXTENDED_CACHE SELECT * FROM ENTRY_KILL_STATS_EXTENDED;
END
$$
LANGUAGE 'plpgsql';
    

--INSERT INTO cron.job (schedule, command, nodename, nodeport, database, username)
--VALUES ('0 0 * * *', $$CALL refresh_ENTRY_KILL_STATS_EXTENDED_CACHE()$$, 'localhost', 5432, 'DEMFILES', 'REPLACE_ME');
    
CREATE TABLE PLAYER_ROUND_KILL_STATS_CACHE AS SELECT * FROM PLAYER_ROUND_KILL_STATS;

CREATE OR REPLACE FUNCTION refresh_PLAYER_ROUND_KILL_STATS_CACHE() RETURNS void AS
$$
BEGIN
    TRUNCATE PLAYER_ROUND_KILL_STATS_CACHE;
    INSERT INTO PLAYER_ROUND_KILL_STATS_CACHE SELECT * FROM PLAYER_ROUND_KILL_STATS;
END
$$
LANGUAGE 'plpgsql';
    

--INSERT INTO cron.job (schedule, command, nodename, nodeport, database, username)
--VALUES ('0 0 * * *', $$CALL refresh_PLAYER_ROUND_KILL_STATS_CACHE()$$, 'localhost', 5432, 'DEMFILES', 'REPLACE_ME');
    
CREATE TABLE PLAYER_ROUND_DEATH_STATS_CACHE AS SELECT * FROM PLAYER_ROUND_DEATH_STATS;

CREATE OR REPLACE FUNCTION refresh_PLAYER_ROUND_DEATH_STATS_CACHE() RETURNS void AS
$$
BEGIN
    TRUNCATE PLAYER_ROUND_DEATH_STATS_CACHE;
    INSERT INTO PLAYER_ROUND_DEATH_STATS_CACHE SELECT * FROM PLAYER_ROUND_DEATH_STATS;
END
$$
LANGUAGE 'plpgsql';
    

--INSERT INTO cron.job (schedule, command, nodename, nodeport, database, username)
--VALUES ('0 0 * * *', $$CALL refresh_PLAYER_ROUND_DEATH_STATS_CACHE()$$, 'localhost', 5432, 'DEMFILES', 'REPLACE_ME');
    
CREATE TABLE PLAYER_ROUND_ASSIST_STATS_CACHE AS SELECT * FROM PLAYER_ROUND_ASSIST_STATS;

CREATE OR REPLACE FUNCTION refresh_PLAYER_ROUND_ASSIST_STATS_CACHE() RETURNS void AS
$$
BEGIN
    TRUNCATE PLAYER_ROUND_ASSIST_STATS_CACHE;
    INSERT INTO PLAYER_ROUND_ASSIST_STATS_CACHE SELECT * FROM PLAYER_ROUND_ASSIST_STATS;
END
$$
LANGUAGE 'plpgsql';
    

--INSERT INTO cron.job (schedule, command, nodename, nodeport, database, username)
--VALUES ('0 0 * * *', $$CALL refresh_PLAYER_ROUND_ASSIST_STATS_CACHE()$$, 'localhost', 5432, 'DEMFILES', 'REPLACE_ME');
    
CREATE TABLE PLAYER_ROUND_FLASH_ASSIST_STATS_CACHE AS SELECT * FROM PLAYER_ROUND_FLASH_ASSIST_STATS;

CREATE OR REPLACE FUNCTION refresh_PLAYER_ROUND_FLASH_ASSIST_STATS_CACHE() RETURNS void AS
$$
BEGIN
    TRUNCATE PLAYER_ROUND_FLASH_ASSIST_STATS_CACHE;
    INSERT INTO PLAYER_ROUND_FLASH_ASSIST_STATS_CACHE SELECT * FROM PLAYER_ROUND_FLASH_ASSIST_STATS;
END
$$
LANGUAGE 'plpgsql';
    

--INSERT INTO cron.job (schedule, command, nodename, nodeport, database, username)
--VALUES ('0 0 * * *', $$CALL refresh_PLAYER_ROUND_FLASH_ASSIST_STATS_CACHE()$$, 'localhost', 5432, 'DEMFILES', 'REPLACE_ME');
    
CREATE TABLE PLAYER_ROUND_EVENT_STATS_CACHE AS SELECT * FROM PLAYER_ROUND_EVENT_STATS;

CREATE OR REPLACE FUNCTION refresh_PLAYER_ROUND_EVENT_STATS_CACHE() RETURNS void AS
$$
BEGIN
    TRUNCATE PLAYER_ROUND_EVENT_STATS_CACHE;
    INSERT INTO PLAYER_ROUND_EVENT_STATS_CACHE SELECT * FROM PLAYER_ROUND_EVENT_STATS;
END
$$
LANGUAGE 'plpgsql';
    

--INSERT INTO cron.job (schedule, command, nodename, nodeport, database, username)
--VALUES ('0 0 * * *', $$CALL refresh_PLAYER_ROUND_EVENT_STATS_CACHE()$$, 'localhost', 5432, 'DEMFILES', 'REPLACE_ME');
    
CREATE TABLE PLAYER_ROUND_DAMAGE_STATS_CACHE AS SELECT * FROM PLAYER_ROUND_DAMAGE_STATS;

CREATE OR REPLACE FUNCTION refresh_PLAYER_ROUND_DAMAGE_STATS_CACHE() RETURNS void AS
$$
BEGIN
    TRUNCATE PLAYER_ROUND_DAMAGE_STATS_CACHE;
    INSERT INTO PLAYER_ROUND_DAMAGE_STATS_CACHE SELECT * FROM PLAYER_ROUND_DAMAGE_STATS;
END
$$
LANGUAGE 'plpgsql';
    

--INSERT INTO cron.job (schedule, command, nodename, nodeport, database, username)
--VALUES ('0 0 * * *', $$CALL refresh_PLAYER_ROUND_DAMAGE_STATS_CACHE()$$, 'localhost', 5432, 'DEMFILES', 'REPLACE_ME');
    
CREATE TABLE ROUND_STATS_EXTENDED_CACHE AS SELECT * FROM ROUND_STATS_EXTENDED;

CREATE OR REPLACE FUNCTION refresh_ROUND_STATS_EXTENDED_CACHE() RETURNS void AS
$$
BEGIN
    TRUNCATE ROUND_STATS_EXTENDED_CACHE;
    INSERT INTO ROUND_STATS_EXTENDED_CACHE SELECT * FROM ROUND_STATS_EXTENDED;
END
$$
LANGUAGE 'plpgsql';
    

--INSERT INTO cron.job (schedule, command, nodename, nodeport, database, username)
--VALUES ('0 0 * * *', $$CALL refresh_ROUND_STATS_EXTENDED_CACHE()$$, 'localhost', 5432, 'DEMFILES', 'REPLACE_ME');
    
CREATE TABLE PLAYER_ROUND_UTILITY_STATS_CACHE AS SELECT * FROM PLAYER_ROUND_UTILITY_STATS;

CREATE OR REPLACE FUNCTION refresh_PLAYER_ROUND_UTILITY_STATS_CACHE() RETURNS void AS
$$
BEGIN
    TRUNCATE PLAYER_ROUND_UTILITY_STATS_CACHE;
    INSERT INTO PLAYER_ROUND_UTILITY_STATS_CACHE SELECT * FROM PLAYER_ROUND_UTILITY_STATS;
END
$$
LANGUAGE 'plpgsql';
    

--INSERT INTO cron.job (schedule, command, nodename, nodeport, database, username)
--VALUES ('0 0 * * *', $$CALL refresh_PLAYER_ROUND_UTILITY_STATS_CACHE()$$, 'localhost', 5432, 'DEMFILES', 'REPLACE_ME');
    
CREATE TABLE PLAYER_ROUND_EXTENDED_STATS_CACHE AS SELECT * FROM PLAYER_ROUND_EXTENDED_STATS;

CREATE OR REPLACE FUNCTION refresh_PLAYER_ROUND_EXTENDED_STATS_CACHE() RETURNS void AS
$$
BEGIN
    TRUNCATE PLAYER_ROUND_EXTENDED_STATS_CACHE;
    INSERT INTO PLAYER_ROUND_EXTENDED_STATS_CACHE SELECT * FROM PLAYER_ROUND_EXTENDED_STATS;
END
$$
LANGUAGE 'plpgsql';
    

--INSERT INTO cron.job (schedule, command, nodename, nodeport, database, username)
--VALUES ('0 0 * * *', $$CALL refresh_PLAYER_ROUND_EXTENDED_STATS_CACHE()$$, 'localhost', 5432, 'DEMFILES', 'REPLACE_ME');
    
CREATE TABLE PLAYER_MATCH_STATS_CACHE AS SELECT * FROM PLAYER_MATCH_STATS;

CREATE OR REPLACE FUNCTION refresh_PLAYER_MATCH_STATS_CACHE() RETURNS void AS
$$
BEGIN
    TRUNCATE PLAYER_MATCH_STATS_CACHE;
    INSERT INTO PLAYER_MATCH_STATS_CACHE SELECT * FROM PLAYER_MATCH_STATS;
END
$$
LANGUAGE 'plpgsql';
    

--INSERT INTO cron.job (schedule, command, nodename, nodeport, database, username)
--VALUES ('0 0 * * *', $$CALL refresh_PLAYER_MATCH_STATS_CACHE()$$, 'localhost', 5432, 'DEMFILES', 'REPLACE_ME');
    
CREATE TABLE MATCH_RESULTS_CACHE AS SELECT * FROM MATCH_RESULTS;

CREATE OR REPLACE FUNCTION refresh_MATCH_RESULTS_CACHE() RETURNS void AS
$$
BEGIN
    TRUNCATE MATCH_RESULTS_CACHE;
    INSERT INTO MATCH_RESULTS_CACHE SELECT * FROM MATCH_RESULTS;
END
$$
LANGUAGE 'plpgsql';
    

--INSERT INTO cron.job (schedule, command, nodename, nodeport, database, username)
--VALUES ('0 0 * * *', $$CALL refresh_MATCH_RESULTS_CACHE()$$, 'localhost', 5432, 'DEMFILES', 'REPLACE_ME');
    
CREATE TABLE PLAYER_MATCH_RESULTS_CACHE AS SELECT * FROM PLAYER_MATCH_RESULTS;

CREATE OR REPLACE FUNCTION refresh_PLAYER_MATCH_RESULTS_CACHE() RETURNS void AS
$$
BEGIN
    TRUNCATE PLAYER_MATCH_RESULTS_CACHE;
    INSERT INTO PLAYER_MATCH_RESULTS_CACHE SELECT * FROM PLAYER_MATCH_RESULTS;
END
$$
LANGUAGE 'plpgsql';
    

--INSERT INTO cron.job (schedule, command, nodename, nodeport, database, username)
--VALUES ('0 0 * * *', $$CALL refresh_PLAYER_MATCH_RESULTS_CACHE()$$, 'localhost', 5432, 'DEMFILES', 'REPLACE_ME');
    
CREATE TABLE PLAYER_MATCH_STATS_EXTENDED_CACHE AS SELECT * FROM PLAYER_MATCH_STATS_EXTENDED;

CREATE OR REPLACE FUNCTION refresh_PLAYER_MATCH_STATS_EXTENDED_CACHE() RETURNS void AS
$$
BEGIN
    TRUNCATE PLAYER_MATCH_STATS_EXTENDED_CACHE;
    INSERT INTO PLAYER_MATCH_STATS_EXTENDED_CACHE SELECT * FROM PLAYER_MATCH_STATS_EXTENDED;
END
$$
LANGUAGE 'plpgsql';
    

--INSERT INTO cron.job (schedule, command, nodename, nodeport, database, username)
--VALUES ('0 0 * * *', $$CALL refresh_PLAYER_MATCH_STATS_EXTENDED_CACHE()$$, 'localhost', 5432, 'DEMFILES', 'REPLACE_ME');
    
CREATE TABLE ROUND_SHOT_STATS_EXTENDED_CACHE AS SELECT * FROM ROUND_SHOT_STATS_EXTENDED;

CREATE OR REPLACE FUNCTION refresh_ROUND_SHOT_STATS_EXTENDED_CACHE() RETURNS void AS
$$
BEGIN
    TRUNCATE ROUND_SHOT_STATS_EXTENDED_CACHE;
    INSERT INTO ROUND_SHOT_STATS_EXTENDED_CACHE SELECT * FROM ROUND_SHOT_STATS_EXTENDED;
END
$$
LANGUAGE 'plpgsql';
    

--INSERT INTO cron.job (schedule, command, nodename, nodeport, database, username)
--VALUES ('0 0 * * *', $$CALL refresh_ROUND_SHOT_STATS_EXTENDED_CACHE()$$, 'localhost', 5432, 'DEMFILES', 'REPLACE_ME');
    
CREATE TABLE ROUND_HIT_STATS_EXTENDED_CACHE AS SELECT * FROM ROUND_HIT_STATS_EXTENDED;

CREATE OR REPLACE FUNCTION refresh_ROUND_HIT_STATS_EXTENDED_CACHE() RETURNS void AS
$$
BEGIN
    TRUNCATE ROUND_HIT_STATS_EXTENDED_CACHE;
    INSERT INTO ROUND_HIT_STATS_EXTENDED_CACHE SELECT * FROM ROUND_HIT_STATS_EXTENDED;
END
$$
LANGUAGE 'plpgsql';
    

--INSERT INTO cron.job (schedule, command, nodename, nodeport, database, username)
--VALUES ('0 0 * * *', $$CALL refresh_ROUND_HIT_STATS_EXTENDED_CACHE()$$, 'localhost', 5432, 'DEMFILES', 'REPLACE_ME');
    
CREATE TABLE MATCH_SHOT_STATS_EXTENDED_CACHE AS SELECT * FROM MATCH_SHOT_STATS_EXTENDED;

CREATE OR REPLACE FUNCTION refresh_MATCH_SHOT_STATS_EXTENDED_CACHE() RETURNS void AS
$$
BEGIN
    TRUNCATE MATCH_SHOT_STATS_EXTENDED_CACHE;
    INSERT INTO MATCH_SHOT_STATS_EXTENDED_CACHE SELECT * FROM MATCH_SHOT_STATS_EXTENDED;
END
$$
LANGUAGE 'plpgsql';
    

--INSERT INTO cron.job (schedule, command, nodename, nodeport, database, username)
--VALUES ('0 0 * * *', $$CALL refresh_MATCH_SHOT_STATS_EXTENDED_CACHE()$$, 'localhost', 5432, 'DEMFILES', 'REPLACE_ME');
    
CREATE TABLE MATCH_HIT_STATS_EXTENDED_CACHE AS SELECT * FROM MATCH_HIT_STATS_EXTENDED;

CREATE OR REPLACE FUNCTION refresh_MATCH_HIT_STATS_EXTENDED_CACHE() RETURNS void AS
$$
BEGIN
    TRUNCATE MATCH_HIT_STATS_EXTENDED_CACHE;
    INSERT INTO MATCH_HIT_STATS_EXTENDED_CACHE SELECT * FROM MATCH_HIT_STATS_EXTENDED;
END
$$
LANGUAGE 'plpgsql';
    

--INSERT INTO cron.job (schedule, command, nodename, nodeport, database, username)
--VALUES ('0 0 * * *', $$CALL refresh_MATCH_HIT_STATS_EXTENDED_CACHE()$$, 'localhost', 5432, 'DEMFILES', 'REPLACE_ME');
    
CREATE TABLE MAP_SHOT_STATS_EXTENDED_CACHE AS SELECT * FROM MAP_SHOT_STATS_EXTENDED;

CREATE OR REPLACE FUNCTION refresh_MAP_SHOT_STATS_EXTENDED_CACHE() RETURNS void AS
$$
BEGIN
    TRUNCATE MAP_SHOT_STATS_EXTENDED_CACHE;
    INSERT INTO MAP_SHOT_STATS_EXTENDED_CACHE SELECT * FROM MAP_SHOT_STATS_EXTENDED;
END
$$
LANGUAGE 'plpgsql';
    

--INSERT INTO cron.job (schedule, command, nodename, nodeport, database, username)
--VALUES ('0 0 * * *', $$CALL refresh_MAP_SHOT_STATS_EXTENDED_CACHE()$$, 'localhost', 5432, 'DEMFILES', 'REPLACE_ME');
    
CREATE TABLE MAP_HIT_STATS_EXTENDED_CACHE AS SELECT * FROM MAP_HIT_STATS_EXTENDED;

CREATE OR REPLACE FUNCTION refresh_MAP_HIT_STATS_EXTENDED_CACHE() RETURNS void AS
$$
BEGIN
    TRUNCATE MAP_HIT_STATS_EXTENDED_CACHE;
    INSERT INTO MAP_HIT_STATS_EXTENDED_CACHE SELECT * FROM MAP_HIT_STATS_EXTENDED;
END
$$
LANGUAGE 'plpgsql';
    

--INSERT INTO cron.job (schedule, command, nodename, nodeport, database, username)
--VALUES ('0 0 * * *', $$CALL refresh_MAP_HIT_STATS_EXTENDED_CACHE()$$, 'localhost', 5432, 'DEMFILES', 'REPLACE_ME');
    
CREATE TABLE OVERALL_SHOT_STATS_EXTENDED_CACHE AS SELECT * FROM OVERALL_SHOT_STATS_EXTENDED;

CREATE OR REPLACE FUNCTION refresh_OVERALL_SHOT_STATS_EXTENDED_CACHE() RETURNS void AS
$$
BEGIN
    TRUNCATE OVERALL_SHOT_STATS_EXTENDED_CACHE;
    INSERT INTO OVERALL_SHOT_STATS_EXTENDED_CACHE SELECT * FROM OVERALL_SHOT_STATS_EXTENDED;
END
$$
LANGUAGE 'plpgsql';
    

--INSERT INTO cron.job (schedule, command, nodename, nodeport, database, username)
--VALUES ('0 0 * * *', $$CALL refresh_OVERALL_SHOT_STATS_EXTENDED_CACHE()$$, 'localhost', 5432, 'DEMFILES', 'REPLACE_ME');
    
CREATE TABLE OVERALL_HIT_STATS_EXTENDED_CACHE AS SELECT * FROM OVERALL_HIT_STATS_EXTENDED;

CREATE OR REPLACE FUNCTION refresh_OVERALL_HIT_STATS_EXTENDED_CACHE() RETURNS void AS
$$
BEGIN
    TRUNCATE OVERALL_HIT_STATS_EXTENDED_CACHE;
    INSERT INTO OVERALL_HIT_STATS_EXTENDED_CACHE SELECT * FROM OVERALL_HIT_STATS_EXTENDED;
END
$$
LANGUAGE 'plpgsql';
    

--INSERT INTO cron.job (schedule, command, nodename, nodeport, database, username)
--VALUES ('0 0 * * *', $$CALL refresh_OVERALL_HIT_STATS_EXTENDED_CACHE()$$, 'localhost', 5432, 'DEMFILES', 'REPLACE_ME');
    
CREATE TABLE ROUND_PLAYER_WEAPON_STATS_CACHE AS SELECT * FROM ROUND_PLAYER_WEAPON_STATS;

CREATE OR REPLACE FUNCTION refresh_ROUND_PLAYER_WEAPON_STATS_CACHE() RETURNS void AS
$$
BEGIN
    TRUNCATE ROUND_PLAYER_WEAPON_STATS_CACHE;
    INSERT INTO ROUND_PLAYER_WEAPON_STATS_CACHE SELECT * FROM ROUND_PLAYER_WEAPON_STATS;
END
$$
LANGUAGE 'plpgsql';
    

--INSERT INTO cron.job (schedule, command, nodename, nodeport, database, username)
--VALUES ('0 0 * * *', $$CALL refresh_ROUND_PLAYER_WEAPON_STATS_CACHE()$$, 'localhost', 5432, 'DEMFILES', 'REPLACE_ME');
    
CREATE TABLE MATCH_PLAYER_WEAPON_STATS_CACHE AS SELECT * FROM MATCH_PLAYER_WEAPON_STATS;

CREATE OR REPLACE FUNCTION refresh_MATCH_PLAYER_WEAPON_STATS_CACHE() RETURNS void AS
$$
BEGIN
    TRUNCATE MATCH_PLAYER_WEAPON_STATS_CACHE;
    INSERT INTO MATCH_PLAYER_WEAPON_STATS_CACHE SELECT * FROM MATCH_PLAYER_WEAPON_STATS;
END
$$
LANGUAGE 'plpgsql';
    

--INSERT INTO cron.job (schedule, command, nodename, nodeport, database, username)
--VALUES ('0 0 * * *', $$CALL refresh_MATCH_PLAYER_WEAPON_STATS_CACHE()$$, 'localhost', 5432, 'DEMFILES', 'REPLACE_ME');
    
CREATE TABLE OVERALL_PLAYER_WEAPON_STATS_CACHE AS SELECT * FROM OVERALL_PLAYER_WEAPON_STATS;

CREATE OR REPLACE FUNCTION refresh_OVERALL_PLAYER_WEAPON_STATS_CACHE() RETURNS void AS
$$
BEGIN
    TRUNCATE OVERALL_PLAYER_WEAPON_STATS_CACHE;
    INSERT INTO OVERALL_PLAYER_WEAPON_STATS_CACHE SELECT * FROM OVERALL_PLAYER_WEAPON_STATS;
END
$$
LANGUAGE 'plpgsql';
    

--INSERT INTO cron.job (schedule, command, nodename, nodeport, database, username)
--VALUES ('0 0 * * *', $$CALL refresh_OVERALL_PLAYER_WEAPON_STATS_CACHE()$$, 'localhost', 5432, 'DEMFILES', 'REPLACE_ME');
    
CREATE TABLE MAP_PLAYER_WEAPON_STATS_CACHE AS SELECT * FROM MAP_PLAYER_WEAPON_STATS;

CREATE OR REPLACE FUNCTION refresh_MAP_PLAYER_WEAPON_STATS_CACHE() RETURNS void AS
$$
BEGIN
    TRUNCATE MAP_PLAYER_WEAPON_STATS_CACHE;
    INSERT INTO MAP_PLAYER_WEAPON_STATS_CACHE SELECT * FROM MAP_PLAYER_WEAPON_STATS;
END
$$
LANGUAGE 'plpgsql';
    

--INSERT INTO cron.job (schedule, command, nodename, nodeport, database, username)
--VALUES ('0 0 * * *', $$CALL refresh_MAP_PLAYER_WEAPON_STATS_CACHE()$$, 'localhost', 5432, 'DEMFILES', 'REPLACE_ME');
    
CREATE TABLE ROUND_SCORECARD_CACHE AS SELECT * FROM ROUND_SCORECARD;

CREATE OR REPLACE FUNCTION refresh_ROUND_SCORECARD_CACHE() RETURNS void AS
$$
BEGIN
    TRUNCATE ROUND_SCORECARD_CACHE;
    INSERT INTO ROUND_SCORECARD_CACHE SELECT * FROM ROUND_SCORECARD;
END
$$
LANGUAGE 'plpgsql';
    

--INSERT INTO cron.job (schedule, command, nodename, nodeport, database, username)
--VALUES ('0 0 * * *', $$CALL refresh_ROUND_SCORECARD_CACHE()$$, 'localhost', 5432, 'DEMFILES', 'REPLACE_ME');
    
CREATE TABLE PLAYER_OVERALL_STATS_EXTENDED_CACHE AS SELECT * FROM PLAYER_OVERALL_STATS_EXTENDED;

CREATE OR REPLACE FUNCTION refresh_PLAYER_OVERALL_STATS_EXTENDED_CACHE() RETURNS void AS
$$
BEGIN
    TRUNCATE PLAYER_OVERALL_STATS_EXTENDED_CACHE;
    INSERT INTO PLAYER_OVERALL_STATS_EXTENDED_CACHE SELECT * FROM PLAYER_OVERALL_STATS_EXTENDED;
END
$$
LANGUAGE 'plpgsql';
    

--INSERT INTO cron.job (schedule, command, nodename, nodeport, database, username)
--VALUES ('0 0 * * *', $$CALL refresh_PLAYER_OVERALL_STATS_EXTENDED_CACHE()$$, 'localhost', 5432, 'DEMFILES', 'REPLACE_ME');
    
CREATE TABLE PLAYER_OVERALL_MATCH_STATS_CACHE AS SELECT * FROM PLAYER_OVERALL_MATCH_STATS;

CREATE OR REPLACE FUNCTION refresh_PLAYER_OVERALL_MATCH_STATS_CACHE() RETURNS void AS
$$
BEGIN
    TRUNCATE PLAYER_OVERALL_MATCH_STATS_CACHE;
    INSERT INTO PLAYER_OVERALL_MATCH_STATS_CACHE SELECT * FROM PLAYER_OVERALL_MATCH_STATS;
END
$$
LANGUAGE 'plpgsql';
    

--INSERT INTO cron.job (schedule, command, nodename, nodeport, database, username)
--VALUES ('0 0 * * *', $$CALL refresh_PLAYER_OVERALL_MATCH_STATS_CACHE()$$, 'localhost', 5432, 'DEMFILES', 'REPLACE_ME');
    
CREATE TABLE PLAYER_WEAPON_RANKING_CACHE AS SELECT * FROM PLAYER_WEAPON_RANKING;

CREATE OR REPLACE FUNCTION refresh_PLAYER_WEAPON_RANKING_CACHE() RETURNS void AS
$$
BEGIN
    TRUNCATE PLAYER_WEAPON_RANKING_CACHE;
    INSERT INTO PLAYER_WEAPON_RANKING_CACHE SELECT * FROM PLAYER_WEAPON_RANKING;
END
$$
LANGUAGE 'plpgsql';
    

--INSERT INTO cron.job (schedule, command, nodename, nodeport, database, username)
--VALUES ('0 0 * * *', $$CALL refresh_PLAYER_WEAPON_RANKING_CACHE()$$, 'localhost', 5432, 'DEMFILES', 'REPLACE_ME');
    
CREATE TABLE PLAYER_MAP_STATS_EXTENDED_CACHE AS SELECT * FROM PLAYER_MAP_STATS_EXTENDED;

CREATE OR REPLACE FUNCTION refresh_PLAYER_MAP_STATS_EXTENDED_CACHE() RETURNS void AS
$$
BEGIN
    TRUNCATE PLAYER_MAP_STATS_EXTENDED_CACHE;
    INSERT INTO PLAYER_MAP_STATS_EXTENDED_CACHE SELECT * FROM PLAYER_MAP_STATS_EXTENDED;
END
$$
LANGUAGE 'plpgsql';
    

--INSERT INTO cron.job (schedule, command, nodename, nodeport, database, username)
--VALUES ('0 0 * * *', $$CALL refresh_PLAYER_MAP_STATS_EXTENDED_CACHE()$$, 'localhost', 5432, 'DEMFILES', 'REPLACE_ME');
    
CREATE TABLE PLAYER_MAP_MATCH_STATS_CACHE AS SELECT * FROM PLAYER_MAP_MATCH_STATS;

CREATE OR REPLACE FUNCTION refresh_PLAYER_MAP_MATCH_STATS_CACHE() RETURNS void AS
$$
BEGIN
    TRUNCATE PLAYER_MAP_MATCH_STATS_CACHE;
    INSERT INTO PLAYER_MAP_MATCH_STATS_CACHE SELECT * FROM PLAYER_MAP_MATCH_STATS;
END
$$
LANGUAGE 'plpgsql';
    

--INSERT INTO cron.job (schedule, command, nodename, nodeport, database, username)
--VALUES ('0 0 * * *', $$CALL refresh_PLAYER_MAP_MATCH_STATS_CACHE()$$, 'localhost', 5432, 'DEMFILES', 'REPLACE_ME');
    
CREATE TABLE PLAYER_WEAPON_MAP_RANKING_CACHE AS SELECT * FROM PLAYER_WEAPON_MAP_RANKING;

CREATE OR REPLACE FUNCTION refresh_PLAYER_WEAPON_MAP_RANKING_CACHE() RETURNS void AS
$$
BEGIN
    TRUNCATE PLAYER_WEAPON_MAP_RANKING_CACHE;
    INSERT INTO PLAYER_WEAPON_MAP_RANKING_CACHE SELECT * FROM PLAYER_WEAPON_MAP_RANKING;
END
$$
LANGUAGE 'plpgsql';
    

--INSERT INTO cron.job (schedule, command, nodename, nodeport, database, username)
--VALUES ('0 0 * * *', $$CALL refresh_PLAYER_WEAPON_MAP_RANKING_CACHE()$$, 'localhost', 5432, 'DEMFILES', 'REPLACE_ME');
    
CREATE TABLE PLAYER_MAP_STATS_EXTENDED_EXTENDED_CACHE AS SELECT * FROM PLAYER_MAP_STATS_EXTENDED_EXTENDED;

CREATE OR REPLACE FUNCTION refresh_PLAYER_MAP_STATS_EXTENDED_EXTENDED_CACHE() RETURNS void AS
$$
BEGIN
    TRUNCATE PLAYER_MAP_STATS_EXTENDED_EXTENDED_CACHE;
    INSERT INTO PLAYER_MAP_STATS_EXTENDED_EXTENDED_CACHE SELECT * FROM PLAYER_MAP_STATS_EXTENDED_EXTENDED;
END
$$
LANGUAGE 'plpgsql';
    

--INSERT INTO cron.job (schedule, command, nodename, nodeport, database, username)
--VALUES ('0 0 * * *', $$CALL refresh_PLAYER_MAP_STATS_EXTENDED_EXTENDED_CACHE()$$, 'localhost', 5432, 'DEMFILES', 'REPLACE_ME');
    
CREATE TABLE PLAYER_CLUTCH_STATS_CACHE AS SELECT * FROM PLAYER_CLUTCH_STATS;

CREATE OR REPLACE FUNCTION refresh_PLAYER_CLUTCH_STATS_CACHE() RETURNS void AS
$$
BEGIN
    TRUNCATE PLAYER_CLUTCH_STATS_CACHE;
    INSERT INTO PLAYER_CLUTCH_STATS_CACHE SELECT * FROM PLAYER_CLUTCH_STATS;
END
$$
LANGUAGE 'plpgsql';
    

--INSERT INTO cron.job (schedule, command, nodename, nodeport, database, username)
--VALUES ('0 0 * * *', $$CALL refresh_PLAYER_CLUTCH_STATS_CACHE()$$, 'localhost', 5432, 'DEMFILES', 'REPLACE_ME');
    
CREATE TABLE PLAYER_OVERALL_STATS_EXTENDED_EXTENDED_CACHE AS SELECT * FROM PLAYER_OVERALL_STATS_EXTENDED_EXTENDED;

CREATE OR REPLACE FUNCTION refresh_PLAYER_OVERALL_STATS_EXTENDED_EXTENDED_CACHE() RETURNS void AS
$$
BEGIN
    TRUNCATE PLAYER_OVERALL_STATS_EXTENDED_EXTENDED_CACHE;
    INSERT INTO PLAYER_OVERALL_STATS_EXTENDED_EXTENDED_CACHE SELECT * FROM PLAYER_OVERALL_STATS_EXTENDED_EXTENDED;
END
$$
LANGUAGE 'plpgsql';
    

--INSERT INTO cron.job (schedule, command, nodename, nodeport, database, username)
--VALUES ('0 0 * * *', $$CALL refresh_PLAYER_OVERALL_STATS_EXTENDED_EXTENDED_CACHE()$$, 'localhost', 5432, 'DEMFILES', 'REPLACE_ME');
    