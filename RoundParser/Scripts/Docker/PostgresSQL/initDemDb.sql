/*
 * Create the Database
 */
CREATE DATABASE DEMFILES;

/*
 * Select the database
 */
\c demfiles;

/*
 * Create the users table
 */
CREATE TABLE USERS (
	STEAM_ID				VARCHAR(100) 		DEFAULT ''	NOT NULL PRIMARY KEY,
	USER_NAME				VARCHAR(100) 		DEFAULT ''	NOT NULL
);

/*
 * Create the users scores table
 */
CREATE TABLE USERS_SCORES (
	GAME_DATE				TIMESTAMP						NOT NULL,
	MAP						VARCHAR(100) 		DEFAULT ''	NOT NULL,
	STEAM_ID				VARCHAR(100) 		DEFAULT ''	NOT NULL,
	SCORE					INTEGER				DEFAULT 0	NOT NULL,
	PRIMARY KEY(GAME_DATE, MAP, STEAM_ID)
);
