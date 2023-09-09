/*
 * Terminating all the connections
 */

SELECT pg_terminate_backend(pg_stat_activity.pid) FROM pg_stat_activity WHERE pg_stat_activity.datname = 'demfiles';

DROP DATABASE IF EXISTS DEMFILES;

/*
 * Create the Database
 */
CREATE DATABASE DEMFILES;

/*
 * Select the database
 */
\c demfiles;

/*
 * BASE TABLES
 */

DROP TABLE IF EXISTS round_stats CASCADE;
DROP TABLE IF EXISTS round_shot_events CASCADE;
DROP TABLE IF EXISTS round_kill_events CASCADE;
DROP TABLE IF EXISTS round_hit_events CASCADE;
DROP TABLE IF EXISTS round_events CASCADE;
DROP TABLE IF EXISTS player_stats CASCADE;
DROP TABLE IF EXISTS player_round_stats CASCADE;
DROP TABLE IF EXISTS match_stats CASCADE;

/*
 * Create the users table
 */
CREATE TABLE USERS (
	STEAM_ID				VARCHAR(100) 		DEFAULT ''	NOT NULL PRIMARY KEY,
	USER_NAME				VARCHAR(100) 		DEFAULT ''	NOT NULL
);

/*
 * Queue of file to process
 */
CREATE TABLE DEM_PROCESS_QUEUE (
    FILE_NAME               VARCHAR(1000)       DEFAULT ''  NOT NULL PRIMARY KEY,
    QUEUED_ON               TIMESTAMP                       NOT NULL,
    PROCESSED_ON            TIMESTAMP,
    PROCESS_STATUS          VARCHAR(50)         DEFAULT ''  NOT NULL
);

CREATE TABLE MATCH_STATS (
    match_fileName          VARCHAR(255),
    match_date              TIMESTAMP,
    mapName                 VARCHAR(255),
    match_id                SERIAL PRIMARY KEY
);

CREATE TABLE PLAYER_ROUND_STATS (
    userName                VARCHAR(255),
    steamID                 VARCHAR(255),
    round                   INTEGER,
    team                    INTEGER,
    clutchChance            NUMERIC,
    clutchSuccess           BOOLEAN,
    survived                BOOLEAN,
    moneySpent              INTEGER,
    equipmentValue          INTEGER,
    mvp                     BOOLEAN,
    match_id                INTEGER,
    FOREIGN KEY (match_id) REFERENCES MATCH_STATS(match_id)
);

CREATE TABLE ROUND_EVENTS (
    eventType               VARCHAR(255),
    eventtime               NUMERIC(10, 6), 
    steamID                 VARCHAR(255),
    round                   INTEGER,
    match_id                INTEGER,
    FOREIGN KEY (match_id) REFERENCES MATCH_STATS(match_id)
);

CREATE TABLE ROUND_KILL_EVENTS (
    eventtime               NUMERIC(10, 6), 
    steamID                 VARCHAR(255),
    assister                VARCHAR(255),
    flashAssister           VARCHAR(255),
    killerFlashed           VARCHAR(255),
    round                   INTEGER,
    weapon                  VARCHAR(255),
    headshot                BOOLEAN,
    victimSteamId           VARCHAR(255),
    isFirstKill             BOOLEAN,
    isTradeKill             BOOLEAN,
    isTradeDeath            BOOLEAN,
    match_id                INTEGER,
    FOREIGN KEY (match_id) REFERENCES MATCH_STATS(match_id)
);

CREATE TABLE ROUND_SHOT_EVENTS (
    eventType               VARCHAR(255),
    eventtime               NUMERIC(10, 6), 
    steamID                 VARCHAR(255),
    round                   INTEGER,
    weapon                  VARCHAR(255),
    match_id                INTEGER,
    FOREIGN KEY (match_id) REFERENCES MATCH_STATS(match_id)
);

CREATE TABLE ROUND_HIT_EVENTS (
    eventtime               NUMERIC(10, 6), 
    steamID                 VARCHAR(255),
    round                   INTEGER,
    weapon                  VARCHAR(255),
    victimSteamId           VARCHAR(255),
    hitGroup                INTEGER,
    damageHealth            INTEGER,
    damageArmour            INTEGER,
    blindTime               NUMERIC(6, 2),
    match_id                INTEGER,
    FOREIGN KEY (match_id) REFERENCES MATCH_STATS(match_id)
);

CREATE TABLE PLAYER_STATS (
    userName                VARCHAR(255),
    steamID                 VARCHAR(255),
    match_id                INTEGER,
    score                   INTEGER,
    FOREIGN KEY (match_id) REFERENCES MATCH_STATS(match_id)
);

CREATE TABLE ROUND_STATS (
    roundNumber             INTEGER,
    winnerSide              INTEGER,
    reasonEndRound          INTEGER,
    match_id                INTEGER,
    FOREIGN KEY (match_id) REFERENCES MATCH_STATS(match_id)
);
