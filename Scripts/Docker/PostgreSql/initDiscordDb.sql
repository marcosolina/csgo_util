/*
 * Terminating all the connections
 */

SELECT pg_terminate_backend(pg_stat_activity.pid) FROM pg_stat_activity WHERE pg_stat_activity.datname = 'discordbot';

DROP DATABASE IF EXISTS DISCORDBOT;

/*
 * Create the Database
 */
CREATE DATABASE DISCORDBOT;

/*
 * Select the database
 */
\c discordbot;

/*
 * Create the users table
 */
CREATE TABLE STEAMMAP (
    DISCORD_ID             BIGINT          DEFAULT 0   NOT NULL PRIMARY KEY,
    STEAM_ID               VARCHAR(100)    DEFAULT ''  NOT NULL,
    DISCORD_NAME           VARCHAR(50)     DEFAULT ''  NOT NULL,
    STEAM_NAME             VARCHAR(50)     DEFAULT ''  NOT NULL
);

