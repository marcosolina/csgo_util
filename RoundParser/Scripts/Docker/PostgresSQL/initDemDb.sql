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
	RWS						NUMERIC(6, 2)		DEFAULT 0	NOT NULL, -- Round Win Share
	KILLS					INTEGER				DEFAULT 0	NOT NULL,
	ASSISTS					INTEGER				DEFAULT 0	NOT NULL,
	DEATHS					INTEGER				DEFAULT 0	NOT NULL,
	KDR						NUMERIC(6, 2)		DEFAULT 0	NOT NULL, -- Kill / Death Ratio
	HS						INTEGER				DEFAULT 0	NOT NULL, -- Head Shots
	HSP						NUMERIC(6, 2)		DEFAULT 0	NOT NULL, -- Head Shots Percentage
	FF						INTEGER				DEFAULT 0	NOT NULL, -- Team Kill (Friendly Fire)
	EK						INTEGER				DEFAULT 0	NOT NULL, -- Entry Kill
	BP						INTEGER				DEFAULT 0	NOT NULL, -- Bomb Planted
	BD						INTEGER				DEFAULT 0	NOT NULL, -- Bomb Defused
	MVP						INTEGER				DEFAULT 0	NOT NULL, -- Most Valuable Player
	SCORE					INTEGER				DEFAULT 0	NOT NULL,
	HLTV					NUMERIC(6, 3)		DEFAULT 0	NOT NULL, -- HLTV Half Life Television Rating
	_5K						INTEGER				DEFAULT 0	NOT NULL, -- Five Kills
	_4K						INTEGER				DEFAULT 0	NOT NULL, -- Four Kills
	_3K						INTEGER				DEFAULT 0	NOT NULL, -- Three Kills
	_2K						INTEGER				DEFAULT 0	NOT NULL, -- Two Kills
	_1K						INTEGER				DEFAULT 0	NOT NULL, -- One Kill
	TK						INTEGER				DEFAULT 0	NOT NULL, -- Trade Kill
	TD						INTEGER				DEFAULT 0	NOT NULL, -- Trade Death
	KPR						NUMERIC(6, 2)		DEFAULT 0	NOT NULL, -- Kill Per Round
	APR						NUMERIC(6, 2)		DEFAULT 0	NOT NULL, -- Assists Per Round
	DPR						NUMERIC(6, 2)		DEFAULT 0	NOT NULL, -- Death Per Round
	ADR						NUMERIC(6, 2)		DEFAULT 0	NOT NULL, -- Average Damage Per Round
	TDH						INTEGER				DEFAULT 0	NOT NULL, -- Total Damage Health
	TDA						INTEGER				DEFAULT 0	NOT NULL, -- Total Damage Armor
	_1V1					INTEGER				DEFAULT 0	NOT NULL, -- 1 Versus 1
	_1V2					INTEGER				DEFAULT 0	NOT NULL, -- 1 Versus 2
	_1V3					INTEGER				DEFAULT 0	NOT NULL, -- 1 Versus 3
	_1V4					INTEGER				DEFAULT 0	NOT NULL, -- 1 Versus 4
	_1V5					INTEGER				DEFAULT 0	NOT NULL, -- 1 Versus 5
	GRENADES				INTEGER				DEFAULT 0	NOT NULL, -- Grenades Thrown count
	FLASHES					INTEGER				DEFAULT 0	NOT NULL, -- Flahses Thrown count
	SMOKES					INTEGER				DEFAULT 0	NOT NULL, -- Smokes Thrown count
	FIRE					INTEGER				DEFAULT 0	NOT NULL, -- Molotov or Incendiary Thrown count
	HED						INTEGER				DEFAULT 0	NOT NULL, -- High Explosive Damage
	FD						INTEGER				DEFAULT 0	NOT NULL, -- Fire Damage
	MP						NUMERIC(6, 2)		DEFAULT 0	NOT NULL, -- Match Played Percentage
	PRIMARY KEY(GAME_DATE, MAP, STEAM_ID)
);
