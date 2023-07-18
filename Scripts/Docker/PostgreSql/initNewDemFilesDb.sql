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
    match_fileName VARCHAR(255),
    match_date TIMESTAMP,
    mapName VARCHAR(255),
    match_id SERIAL PRIMARY KEY
);

--UPDATE MATCH_STATS 
--SET 
--    match_date = TO_TIMESTAMP(
--        SUBSTRING((regexp_match(match_id, 'auto0-(\d{8}-\d{6})'))[1] FROM 1 FOR 8) || ' ' || 
--        LEFT(SUBSTRING((regexp_match(match_id, 'auto0-(\d{8}-\d{6})'))[1] FROM 10), 2) || ':' ||
--        SUBSTRING(SUBSTRING((regexp_match(match_id, 'auto0-(\d{8}-\d{6})'))[1] FROM 10), 3, 2) || ':' ||
--        RIGHT(SUBSTRING((regexp_match(match_id, 'auto0-(\d{8}-\d{6})'))[1] FROM 10), 2), 
--        'YYYYMMDD HH24:MI:SS'),
--    mapName = (regexp_match(match_id, 'auto0-(\d{8}-\d{6})-.*-(.*?)-IXI'))[2];


CREATE TABLE PLAYER_ROUND_STATS (
    userName VARCHAR(255),
    steamID VARCHAR(255),
    round INTEGER,
    team INTEGER,
    clutchChance NUMERIC,
    clutchSuccess BOOLEAN,
    survived BOOLEAN,
    moneySpent INTEGER,
    equipmentValue INTEGER,
    mvp BOOLEAN,
    match_id INTEGER,
    FOREIGN KEY (match_id) REFERENCES MATCH_STATS(match_id)
);

CREATE TABLE ROUND_EVENTS (
    eventType VARCHAR(255),
    eventtime NUMERIC(10, 6), 
    steamID VARCHAR(255),
    round INTEGER,
    match_id INTEGER,
    FOREIGN KEY (match_id) REFERENCES MATCH_STATS(match_id)
);

CREATE TABLE ROUND_KILL_EVENTS (
    eventtime NUMERIC(10, 6), 
    steamID VARCHAR(255),
    assister VARCHAR(255),
    flashAssister VARCHAR(255),
    killerFlashed VARCHAR(255),
    round INTEGER,
    weapon VARCHAR(255),
    headshot BOOLEAN,
    victimSteamId VARCHAR(255),
    isFirstKill BOOLEAN,
    isTradeKill BOOLEAN,
    isTradeDeath BOOLEAN,
    match_id INTEGER,
    FOREIGN KEY (match_id) REFERENCES MATCH_STATS(match_id)
);

CREATE TABLE ROUND_SHOT_EVENTS (
    eventType VARCHAR(255),
    eventtime NUMERIC(10, 6), 
    steamID VARCHAR(255),
    round INTEGER,
    weapon VARCHAR(255),
    match_id INTEGER,
    FOREIGN KEY (match_id) REFERENCES MATCH_STATS(match_id)
);

CREATE TABLE ROUND_HIT_EVENTS (
    eventtime NUMERIC(10, 6), 
    steamID VARCHAR(255),
    round INTEGER,
    weapon VARCHAR(255),
    victimSteamId VARCHAR(255),
    hitGroup INTEGER,
    damageHealth INTEGER,
    damageArmour INTEGER,
    blindTime NUMERIC(6, 2),
    match_id INTEGER,
    FOREIGN KEY (match_id) REFERENCES MATCH_STATS(match_id)
);

CREATE TABLE PLAYER_STATS (
    userName VARCHAR(255),
    steamID VARCHAR(255),
    match_id INTEGER,
    score INTEGER,
    FOREIGN KEY (match_id) REFERENCES MATCH_STATS(match_id)
);

CREATE TABLE ROUND_STATS (
    roundNumber INTEGER,
    winnerSide INTEGER,
    reasonEndRound INTEGER,
    match_id INTEGER,
    FOREIGN KEY (match_id) REFERENCES MATCH_STATS(match_id)
);

DROP VIEW IF EXISTS ROUND_KILL_EVENTS_EXTENDED CASCADE;
DROP VIEW IF EXISTS PLAYER_KILL_COUNT CASCADE;
DROP VIEW IF EXISTS PLAYER_MATCH_KILL_COUNT CASCADE;
DROP VIEW IF EXISTS PLAYER_WEAPON_MAP_KILLS CASCADE;
DROP VIEW IF EXISTS PLAYER_WEAPON_MATCH_KILLS CASCADE;
DROP VIEW IF EXISTS PLAYER_WEAPON_OVERALL_KILLS CASCADE;
DROP VIEW IF EXISTS ROUND_HIT_EVENTS_EXTENDED CASCADE;
DROP VIEW IF EXISTS ENTRY_KILL_STATS CASCADE;
DROP VIEW IF EXISTS ENTRY_KILL_STATS_EXTENDED CASCADE;
DROP VIEW IF EXISTS PLAYER_ROUND_KILL_STATS CASCADE;
DROP VIEW IF EXISTS PLAYER_ROUND_DEATH_STATS CASCADE;
DROP VIEW IF EXISTS PLAYER_ROUND_ASSIST_STATS CASCADE;
DROP VIEW IF EXISTS PLAYER_ROUND_FLASH_ASSIST_STATS CASCADE;
DROP VIEW IF EXISTS PLAYER_ROUND_EVENT_STATS CASCADE;
DROP VIEW IF EXISTS PLAYER_ROUND_DAMAGE_STATS CASCADE;
DROP VIEW IF EXISTS ROUND_STATS_EXTENDED CASCADE;
DROP VIEW IF EXISTS PLAYER_ROUND_UTILITY_STATS CASCADE;
DROP VIEW IF EXISTS PLAYER_ROUND_EXTENDED_STATS CASCADE;
DROP VIEW IF EXISTS PLAYER_MATCH_STATS CASCADE;
DROP VIEW IF EXISTS MATCH_RESULTS CASCADE;
DROP VIEW IF EXISTS PLAYER_MATCH_RESULTS CASCADE;
DROP VIEW IF EXISTS PLAYER_MATCH_STATS_EXTENDED CASCADE;
DROP VIEW IF EXISTS ROUND_SHOT_STATS_EXTENDED CASCADE;
DROP VIEW IF EXISTS ROUND_HIT_STATS_EXTENDED CASCADE;
DROP VIEW IF EXISTS MATCH_SHOT_STATS_EXTENDED CASCADE;
DROP VIEW IF EXISTS MATCH_HIT_STATS_EXTENDED CASCADE;
DROP VIEW IF EXISTS MAP_SHOT_STATS_EXTENDED CASCADE;
DROP VIEW IF EXISTS MAP_HIT_STATS_EXTENDED CASCADE;
DROP VIEW IF EXISTS OVERALL_SHOT_STATS_EXTENDED CASCADE;
DROP VIEW IF EXISTS OVERALL_HIT_STATS_EXTENDED CASCADE;
DROP VIEW IF EXISTS ROUND_PLAYER_WEAPON_STATS CASCADE;
DROP VIEW IF EXISTS MATCH_PLAYER_WEAPON_STATS CASCADE;
DROP VIEW IF EXISTS OVERALL_PLAYER_WEAPON_STATS CASCADE;
DROP VIEW IF EXISTS MAP_PLAYER_WEAPON_STATS CASCADE;
DROP VIEW IF EXISTS ROUND_SCORECARD CASCADE;
DROP VIEW IF EXISTS PLAYER_OVERALL_STATS_EXTENDED CASCADE;
DROP VIEW IF EXISTS PLAYER_OVERALL_MATCH_STATS CASCADE;
DROP VIEW IF EXISTS PLAYER_WEAPON_RANKING CASCADE;
DROP VIEW IF EXISTS PLAYER_MAP_STATS_EXTENDED CASCADE;
DROP VIEW IF EXISTS PLAYER_MAP_MATCH_STATS CASCADE;
DROP VIEW IF EXISTS PLAYER_WEAPON_MAP_RANKING CASCADE;
DROP VIEW IF EXISTS PLAYER_MAP_STATS_EXTENDED_EXTENDED CASCADE;
DROP VIEW IF EXISTS PLAYER_CLUTCH_STATS CASCADE;
DROP VIEW IF EXISTS PLAYER_OVERALL_STATS_EXTENDED_EXTENDED CASCADE;


CREATE OR REPLACE VIEW ROUND_KILL_EVENTS_EXTENDED AS
SELECT 
    rke.*, 
    prs_killer.team AS killer_team, 
    prs_victim.team AS victim_team
FROM 
    ROUND_KILL_EVENTS rke
LEFT JOIN 
    PLAYER_ROUND_STATS prs_killer ON rke.steamID = prs_killer.steamID AND rke.match_id = prs_killer.match_id AND rke.round = prs_killer.round
LEFT JOIN 
    PLAYER_ROUND_STATS prs_victim ON rke.victimSteamId = prs_victim.steamID AND rke.match_id = prs_victim.match_id AND rke.round = prs_victim.round;

CREATE OR REPLACE VIEW PLAYER_KILL_COUNT AS
SELECT 
    rkee.steamID AS killer, 
    rkee.victimSteamId AS victim, 
    COUNT(*) AS kill_count
FROM 
    ROUND_KILL_EVENTS_EXTENDED rkee
WHERE 
    rkee.killer_team != rkee.victim_team
GROUP BY 
    rkee.steamID, 
    rkee.victimSteamId;

CREATE OR REPLACE VIEW PLAYER_MATCH_KILL_COUNT AS
SELECT 
    rkee.steamID AS killer, 
    rkee.victimSteamId AS victim,
    rkee.match_id,
    COUNT(*) AS kill_count
FROM 
    ROUND_KILL_EVENTS_EXTENDED rkee
WHERE 
    rkee.killer_team != rkee.victim_team
GROUP BY 
    rkee.steamID,
    rkee.match_id,
    rkee.victimSteamId;

CREATE OR REPLACE VIEW PLAYER_WEAPON_MAP_KILLS AS
SELECT
    steamID,
    mapName,
    weapon,
    COUNT(*) AS kills,
    SUM(CASE WHEN headshot THEN 1 ELSE 0 END) AS headshots
FROM 
    ROUND_KILL_EVENTS_EXTENDED as r
LEFT JOIN
    MATCH_STATS as m ON r.match_id=m.match_id
WHERE
    killer_team != victim_team
GROUP BY 
    steamID,
    mapName,
    weapon;

CREATE OR REPLACE VIEW PLAYER_WEAPON_MATCH_KILLS AS
SELECT
    steamID,
    match_id,
    weapon,
    COUNT(*) AS kills,
    SUM(CASE WHEN headshot THEN 1 ELSE 0 END) AS headshots
FROM 
    ROUND_KILL_EVENTS_EXTENDED
WHERE
    killer_team != victim_team
GROUP BY 
    steamID,
    match_id,
    weapon;

CREATE OR REPLACE VIEW PLAYER_WEAPON_OVERALL_KILLS AS
SELECT
    steamID,
    weapon,
    COUNT(*) AS kills,
    SUM(CASE WHEN headshot THEN 1 ELSE 0 END) AS headshots
FROM 
    ROUND_KILL_EVENTS_EXTENDED
WHERE
    killer_team != victim_team
GROUP BY 
    steamID,
    weapon;


CREATE OR REPLACE VIEW ROUND_HIT_EVENTS_EXTENDED AS
SELECT 
    rhe.*, 
    prs_attacker.team AS attacker_team, 
    prs_victim.team AS victim_team
FROM 
    ROUND_HIT_EVENTS rhe
LEFT JOIN 
    PLAYER_ROUND_STATS prs_attacker ON rhe.steamID = prs_attacker.steamID AND rhe.match_id = prs_attacker.match_id AND rhe.round = prs_attacker.round
LEFT JOIN 
    PLAYER_ROUND_STATS prs_victim ON rhe.victimSteamId = prs_victim.steamID AND rhe.match_id = prs_victim.match_id AND rhe.round = prs_victim.round;


CREATE OR REPLACE VIEW ENTRY_KILL_STATS AS
SELECT 
    P.steamID,
    (SELECT COUNT(*) FROM (SELECT DISTINCT match_id, round FROM PLAYER_ROUND_STATS WHERE steamID = P.steamID) AS sub) AS total_rounds,
    (SELECT COUNT(*) FROM (SELECT DISTINCT match_id, round FROM PLAYER_ROUND_STATS WHERE steamID = P.steamID AND team = 2) AS sub) AS total_rounds_t,
    (SELECT COUNT(*) FROM (SELECT DISTINCT match_id, round FROM PLAYER_ROUND_STATS WHERE steamID = P.steamID AND team = 3) AS sub) AS total_rounds_ct,
    SUM(CASE WHEN E.isFirstKill AND E.killer_team != E.victim_team THEN 1 ELSE 0 END) AS ek_attempts,
    SUM(CASE WHEN E.isFirstKill AND E.killer_team != E.victim_team AND P.survived THEN 1 ELSE 0 END) AS ek_success,
    SUM(CASE WHEN E.isFirstKill AND P.team = 2 AND E.killer_team != E.victim_team THEN 1 ELSE 0 END) AS ekt_attempts,
    SUM(CASE WHEN E.isFirstKill AND P.team = 2 AND E.killer_team != E.victim_team AND P.survived THEN 1 ELSE 0 END) AS ekt_success,
    SUM(CASE WHEN E.isFirstKill AND P.team = 3 AND E.killer_team != E.victim_team THEN 1 ELSE 0 END) AS ekct_attempts,
    SUM(CASE WHEN E.isFirstKill AND P.team = 3 AND E.killer_team != E.victim_team AND P.survived THEN 1 ELSE 0 END) AS ekct_success
FROM
    PLAYER_ROUND_STATS AS P
LEFT JOIN
    ROUND_KILL_EVENTS_EXTENDED AS E ON E.steamID = P.steamID AND E.round = P.round AND E.match_id = P.match_id
GROUP BY 
    P.steamID;


CREATE OR REPLACE VIEW ENTRY_KILL_STATS_EXTENDED AS
SELECT 
    E.steamID,
    E.total_rounds,
    total_rounds_t,
    total_rounds_ct,
    E.ek_attempts,
    E.ek_success,
    ROUND(CAST(E.ek_success AS NUMERIC) * 100.0 / NULLIF(E.ek_attempts, 0), 2) AS ek_success_rate,
    ROUND(CAST(E.ek_success AS NUMERIC) * 100.0 / NULLIF(E.total_rounds, 0), 2) AS ek_success_rate_overall,
    E.ekt_attempts,
    E.ekt_success,
    ROUND(CAST(E.ekt_success AS NUMERIC) * 100.0 / NULLIF(E.ekt_attempts, 0), 2) AS ekt_success_rate,
    ROUND(CAST(E.ekt_success AS NUMERIC) * 100.0 / NULLIF(E.total_rounds_t, 0), 2) AS ekt_success_rate_overall,
    E.ekct_attempts,
    E.ekct_success,
    ROUND(CAST(E.ekct_success AS NUMERIC) * 100.0 / NULLIF(E.ekct_attempts, 0), 2) AS ekct_success_rate,
    ROUND(CAST(E.ekct_success AS NUMERIC) * 100.0 / NULLIF(E.total_rounds_ct, 0), 2) AS ekct_success_rate_overall
FROM 
    ENTRY_KILL_STATS E;



CREATE OR REPLACE VIEW PLAYER_ROUND_KILL_STATS AS
SELECT
    prs.steamID,
    prs.match_id,
    prs.round,
    COUNT(*) as kills,
    COUNT(CASE WHEN ke.headshot = TRUE THEN 1 END) as headshots,
    CASE 
        WHEN COUNT(*) = 0 THEN 0
        ELSE ROUND((COUNT(CASE WHEN ke.headshot = TRUE THEN 1 END) * 1.0 / COUNT(*)) * 100, 2)
    END as headshot_percentage,
    COUNT(CASE WHEN ke.isTradeKill = TRUE THEN 1 END) as trade_kills,
    COUNT(CASE WHEN ke.killer_team = ke.victim_team THEN 1 END) AS team_kills,
    COUNT(CASE WHEN ke.isFirstKill = TRUE THEN 1 END) as entry_kills
FROM 
    PLAYER_ROUND_STATS prs
INNER JOIN 
    ROUND_KILL_EVENTS_EXTENDED ke ON prs.steamID = ke.steamID AND prs.match_id = ke.match_id AND prs.round = ke.round
WHERE 
    ke.steamID IS NOT NULL
GROUP BY
    prs.steamID,
    prs.match_id,
    prs.round;

CREATE OR REPLACE VIEW PLAYER_ROUND_DEATH_STATS AS
SELECT
    prs.steamID,
    prs.match_id,
    prs.round,
    COUNT(*) as deaths,
    COUNT(CASE WHEN ke.headshot = TRUE THEN 1 END) as headshot_deaths,
    COUNT(CASE WHEN ke.isTradeDeath = TRUE THEN 1 END) as trade_deaths,
    COUNT(CASE WHEN ke.killer_team = ke.victim_team THEN 1 END) AS team_deaths
FROM 
    PLAYER_ROUND_STATS prs
INNER JOIN 
    ROUND_KILL_EVENTS_EXTENDED ke ON prs.steamID = ke.victimSteamId AND prs.match_id = ke.match_id AND prs.round = ke.round
WHERE 
    ke.victimSteamId IS NOT NULL
GROUP BY
    prs.steamID,
    prs.match_id,
    prs.round;

CREATE OR REPLACE VIEW PLAYER_ROUND_ASSIST_STATS AS
SELECT
    prs.steamID,
    prs.match_id,
    prs.round,
    COUNT(*) as assists
FROM 
    PLAYER_ROUND_STATS prs
INNER JOIN 
    ROUND_KILL_EVENTS ke ON prs.steamID = ke.assister AND prs.match_id = ke.match_id AND prs.round = ke.round
WHERE 
    ke.assister IS NOT NULL
GROUP BY
    prs.steamID,
    prs.match_id,
    prs.round;

CREATE OR REPLACE VIEW PLAYER_ROUND_FLASH_ASSIST_STATS AS
SELECT
    prs.steamID,
    prs.match_id,
    prs.round,
    COUNT(*) as flashassists
FROM 
    PLAYER_ROUND_STATS prs
INNER JOIN 
    ROUND_KILL_EVENTS ke ON prs.steamID = ke.flashAssister AND prs.match_id = ke.match_id AND prs.round = ke.round
WHERE 
    ke.flashAssister IS NOT NULL
GROUP BY
    prs.steamID,
    prs.match_id,
    prs.round;

CREATE OR REPLACE VIEW PLAYER_ROUND_EVENT_STATS AS
SELECT
    prs.steamID,
    prs.match_id,
    prs.round,
    COUNT(CASE WHEN re.eventtype = 'bomb_planted' THEN 1 END) as bombs_planted,
    COUNT(CASE WHEN re.eventtype = 'bomb_defused' THEN 1 END) as bombs_defused,
    COUNT(CASE WHEN re.eventtype = 'hostage_rescued' THEN 1 END) as hostages_rescued
FROM 
    PLAYER_ROUND_STATS prs
INNER JOIN 
    ROUND_EVENTS re ON prs.steamID = re.steamID AND prs.match_id = re.match_id AND prs.round = re.round
WHERE 
    re.steamID IS NOT NULL
GROUP BY
    prs.steamID,
    prs.match_id,
    prs.round;


CREATE OR REPLACE VIEW PLAYER_ROUND_DAMAGE_STATS AS
SELECT
    prs.steamID,
    prs.match_id,
    prs.round,
    SUM(CASE WHEN he.attacker_team != he.victim_team THEN he.damageHealth ELSE 0 END) as total_damage_health,
    SUM(CASE WHEN he.attacker_team != he.victim_team THEN he.damageArmour ELSE 0 END) as total_damage_armour,
    SUM(CASE WHEN he.weapon = 'hegrenade' AND he.attacker_team != he.victim_team THEN he.damageHealth ELSE 0 END) as he_damage,
    SUM(CASE WHEN he.weapon = 'inferno' AND he.attacker_team != he.victim_team THEN he.damageHealth ELSE 0 END) as fire_damage,
    SUM(CASE WHEN he.weapon = 'flashbang' AND he.attacker_team != he.victim_team THEN 1 ELSE 0 END) as opponents_flashed,
    SUM(CASE WHEN he.weapon = 'flashbang' AND he.attacker_team != he.victim_team THEN he.blindTime ELSE 0 END) as opponent_blindtime,
    SUM(CASE WHEN he.weapon = 'flashbang' AND he.attacker_team = he.victim_team THEN he.blindTime ELSE 0 END) as teammate_blindtime,
    SUM(CASE WHEN he.attacker_team = he.victim_team THEN he.damageHealth ELSE 0 END) as teammate_damage_health
FROM 
    PLAYER_ROUND_STATS prs
LEFT JOIN 
    ROUND_HIT_EVENTS_EXTENDED he ON prs.steamID = he.steamID AND prs.match_id = he.match_id AND prs.round = he.round
WHERE 
    he.steamID IS NOT NULL
GROUP BY
    prs.steamID,
    prs.match_id,
    prs.round;

CREATE OR REPLACE VIEW ROUND_STATS_EXTENDED AS
SELECT
    rs.match_id,
    rs.roundNumber,
    rs.winnerSide,
    rs.reasonEndRound,
    SUM(CASE WHEN prd.team = rs.winnerSide THEN prd.total_damage_health + prd.total_damage_armour ELSE 0 END) as total_damage_winners
FROM
    ROUND_STATS rs
LEFT JOIN
    (SELECT
        prs.steamID,
        prs.match_id,
        prs.round,
        prs.team,
        prds.total_damage_health,
        prds.total_damage_armour
    FROM
        PLAYER_ROUND_STATS prs
    JOIN
        PLAYER_ROUND_DAMAGE_STATS prds ON prs.steamID = prds.steamID AND prs.match_id = prds.match_id AND prs.round = prds.round
    ) prd ON rs.match_id = prd.match_id AND rs.roundNumber = prd.round
GROUP BY
    rs.match_id,
    rs.roundNumber,
    rs.winnerSide,
    rs.reasonEndRound;


CREATE OR REPLACE VIEW PLAYER_ROUND_UTILITY_STATS AS
SELECT
    prs.steamID,
    prs.match_id,
    prs.round,
    COUNT(CASE WHEN se.weapon = 'weapon_hegrenade' THEN 1 END) as grenades_thrown,
    COUNT(CASE WHEN se.weapon = 'weapon_flashbang' THEN 1 END) as flashes_thrown,
    COUNT(CASE WHEN se.weapon = 'weapon_smokegrenade' THEN 1 END) as smokes_thrown,
    COUNT(CASE WHEN se.weapon = 'weapon_incgrenade' OR se.weapon = 'weapon_molotov' THEN 1 END) as inferno_thrown
FROM 
    PLAYER_ROUND_STATS prs
LEFT JOIN 
    ROUND_SHOT_EVENTS se ON prs.steamID = se.steamID AND prs.match_id = se.match_id AND prs.round = se.round
WHERE 
    se.steamID IS NOT NULL AND se.eventType = 'weapon_fire'
GROUP BY
    prs.steamID,
    prs.match_id,
    prs.round;

CREATE OR REPLACE VIEW PLAYER_ROUND_EXTENDED_STATS AS
SELECT 
    prs.steamID, 
    prs.match_id, 
    prs.round, 
    COALESCE(ke.kills, 0) AS kills,
    COALESCE(ae.assists, 0) AS assists,
    COALESCE(ke.team_kills, 0) AS team_kills,
    COALESCE(ke.entry_kills, 0) AS entry_kills,
    COALESCE(de.deaths, 0) AS deaths,
    COALESCE(de.team_deaths, 0) AS team_deaths,
    COALESCE(de.headshot_deaths, 0) AS headshot_deaths,
    COALESCE(ke.headshots, 0) AS headshots,
    COALESCE(ke.headshot_percentage, 0) AS headshot_percentage,
    COALESCE(ke.trade_kills, 0) AS trade_kills,
    COALESCE(de.trade_deaths, 0) AS trade_deaths,
    COALESCE(he.total_damage_health, 0) AS total_damage_health,
    COALESCE(he.total_damage_armour, 0) AS total_damage_armour,
    COALESCE(he.teammate_damage_health, 0) AS teammate_damage_health,
    COALESCE(prs.clutchChance, 0) AS clutchChance,
    prs.clutchSuccess,
    prs.survived,
    COALESCE(prs.moneySpent, 0) AS moneySpent,
    COALESCE(prs.equipmentValue, 0) AS equipmentValue,
    prs.mvp,
    COALESCE(ue.grenades_thrown, 0) AS grenades_thrown,
    COALESCE(ue.flashes_thrown, 0) AS flashes_thrown,
    COALESCE(ue.smokes_thrown, 0) AS smokes_thrown,
    COALESCE(ue.inferno_thrown, 0) AS inferno_thrown,
    COALESCE(he.he_damage, 0) AS he_damage,
    COALESCE(he.fire_damage, 0) AS fire_damage,
    COALESCE(prs.team, 0) AS team,
    COALESCE(he.opponents_flashed, 0) AS opponents_flashed,
    COALESCE(he.opponent_blindtime, 0) AS opponent_blindtime,
    COALESCE(he.teammate_blindtime, 0) AS teammate_blindtime,
    COALESCE(fa.flashassists, 0) AS flashassists,
    COALESCE(re.bombs_planted, 0) AS bombs_planted,
    COALESCE(re.bombs_defused, 0) AS bombs_defused,
    COALESCE(re.hostages_rescued, 0) AS hostages_rescued,
    CASE WHEN (prs.team = rse.winnerSide AND rse.total_damage_winners>0) THEN 
        COALESCE(ROUND((he.total_damage_health + he.total_damage_armour)*100.0/rse.total_damage_winners,2), 0) 
    ELSE 0 END AS rws,
    CASE WHEN ke.kills>0 OR ae.assists>0 OR prs.survived OR ke.trade_kills>0 THEN 1 ELSE 0 END as kast
FROM 
    PLAYER_ROUND_STATS prs
LEFT JOIN 
    PLAYER_ROUND_KILL_STATS ke ON prs.steamID = ke.steamID AND prs.match_id = ke.match_id AND prs.round = ke.round
LEFT JOIN 
    PLAYER_ROUND_ASSIST_STATS ae ON prs.steamID = ae.steamID AND prs.match_id = ae.match_id AND prs.round = ae.round
LEFT JOIN 
    PLAYER_ROUND_DEATH_STATS de ON prs.steamID = de.steamID AND prs.match_id = de.match_id AND prs.round = de.round
LEFT JOIN 
    PLAYER_ROUND_DAMAGE_STATS he ON prs.steamID = he.steamID AND prs.match_id = he.match_id AND prs.round = he.round
LEFT JOIN 
    PLAYER_ROUND_UTILITY_STATS ue ON prs.steamID = ue.steamID AND prs.match_id = ue.match_id AND prs.round = ue.round
LEFT JOIN 
    PLAYER_ROUND_FLASH_ASSIST_STATS fa ON prs.steamID = fa.steamID AND prs.match_id = fa.match_id AND prs.round = fa.round
LEFT JOIN 
    PLAYER_ROUND_EVENT_STATS re ON prs.steamID = re.steamID AND prs.match_id = re.match_id AND prs.round = re.round
LEFT JOIN
    ROUND_STATS_EXTENDED rse on prs.match_id = rse.match_id AND prs.round = rse.roundNumber;



CREATE OR REPLACE VIEW PLAYER_MATCH_STATS AS
SELECT
    p.steamID,
    m.match_date,
    STRING_AGG(DISTINCT userName, ', ') usernames,
    m.match_id,
    SUM(CASE WHEN r.team > 1 THEN 1 ELSE 0 END) as roundsPlayed,
    SUM(r.kills) as kills,
    SUM(r.assists) as assists,
    SUM(r.deaths) as deaths,
    CASE 
        WHEN SUM(r.deaths) = 0 THEN  SUM(r.kills)
        ELSE ROUND(SUM(r.kills) * 1.0/SUM(r.deaths), 2)
    END as kdr,
    SUM(r.headshots) as headshots,
    CASE 
        WHEN SUM(r.kills) = 0 THEN 0
        ELSE ROUND(SUM(r.headshots) * 1.0 / SUM(r.kills) * 100, 2)
    END as headshot_percentage,
    SUM(r.team_kills) as ff,
    SUM(r.entry_kills) as ek,
    SUM(r.bombs_planted) as bp,
    SUM(r.bombs_defused) as bd,
    SUM(r.hostages_rescued) as hr,
    SUM(CASE WHEN r.mvp = TRUE THEN 1 ELSE 0 END) as mvp,
    p.score,
    SUM(CASE WHEN r.kills>=5 THEN 1 ELSE 0 END) as _5k,
    SUM(CASE WHEN r.kills=4 THEN 1 ELSE 0 END) as _4k,
    SUM(CASE WHEN r.kills=3 THEN 1 ELSE 0 END) as _3k,
    SUM(CASE WHEN r.kills=2 THEN 1 ELSE 0 END) as _2k,
    SUM(CASE WHEN r.kills=1 THEN 1 ELSE 0 END) as _1k,
    SUM(r.trade_kills) as tk,
    SUM(r.trade_deaths) as td,
    SUM(r.total_damage_health) as tdh,
    SUM(r.total_damage_armour) as tda,
    SUM(r.teammate_damage_health) as ffd,
    SUM(r.opponent_blindtime) as ebt,
    SUM(r.teammate_blindtime) as fbt,
    SUM(r.he_damage +r.fire_damage) as ud,
    SUM(CASE WHEN r.clutchChance=1 AND r.clutchSuccess THEN 1 ELSE 0 END) as _1v1,
    SUM(CASE WHEN r.clutchChance=2 AND r.clutchSuccess THEN 1 ELSE 0 END) as _1v2,
    SUM(CASE WHEN r.clutchChance=3 AND r.clutchSuccess THEN 1 ELSE 0 END) as _1v3,
    SUM(CASE WHEN r.clutchChance=4 AND r.clutchSuccess THEN 1 ELSE 0 END) as _1v4,
    SUM(CASE WHEN r.clutchChance=5 AND r.clutchSuccess THEN 1 ELSE 0 END) as _1v5,
    SUM(r.rws) as rwsTotal,
    SUM(r.flashassists) as fa,
    SUM(r.kast) as kastTotal
    
FROM
    PLAYER_STATS p
LEFT JOIN
    PLAYER_ROUND_EXTENDED_STATS r ON p.steamID = r.steamID AND p.match_id = r.match_id
LEFT JOIN
    MATCH_STATS as m ON r.match_id=m.match_id

GROUP BY
    p.steamID,
    m.match_id,
    m.match_date,
    p.score;

CREATE OR REPLACE VIEW MATCH_RESULTS AS (
SELECT 
    m.match_id, 
    m.match_date, 
    m.mapName, 
    SUM(CASE WHEN r.roundNumber <= 7 AND r.winnerSide = 2 THEN 1 ELSE 0 END) AS team1_wins_as_T,
    SUM(CASE WHEN r.roundNumber > 7 AND r.winnerSide = 3 THEN 1 ELSE 0 END) AS team1_wins_as_CT,
    SUM(CASE WHEN r.roundNumber <= 7 AND r.winnerSide = 3 THEN 1 ELSE 0 END) AS team2_wins_as_CT,
    SUM(CASE WHEN r.roundNumber > 7 AND r.winnerSide = 2 THEN 1 ELSE 0 END) AS team2_wins_as_T,
    SUM(CASE WHEN r.winnerSide = 2 THEN 1 ELSE 0 END) AS total_T_wins,
    SUM(CASE WHEN r.winnerSide = 3 THEN 1 ELSE 0 END) AS total_CT_wins,
    SUM(CASE WHEN (r.roundNumber <= 7 AND r.winnerSide = 2) OR (r.roundNumber > 7 AND r.winnerSide = 3) THEN 1 ELSE 0 END) AS team1_total_wins,
    SUM(CASE WHEN (r.roundNumber <= 7 AND r.winnerSide = 3) OR (r.roundNumber > 7 AND r.winnerSide = 2) THEN 1 ELSE 0 END) AS team2_total_wins
FROM 
    MATCH_STATS m
LEFT JOIN 
    ROUND_STATS r ON m.match_id = r.match_id
GROUP BY 
    m.match_id
);

--select mapName,round(avg(total_T_wins)-7.5,1) as Tadvantage from match_results group by mapName;

CREATE OR REPLACE VIEW PLAYER_MATCH_RESULTS AS (
SELECT 
    pt.match_id,
    pt.steamID,
    pt.last_round_team,
    pt.rounds_on_team1,
    pt.rounds_on_team2,
    CASE 
        WHEN pt.last_round_team = 'team1' THEN 
            CASE 
                WHEN mr.team1_total_wins > mr.team2_total_wins THEN 'win'
                WHEN mr.team1_total_wins < mr.team2_total_wins THEN 'loss'
                ELSE 'draw'
            END
        WHEN pt.last_round_team = 'team2' THEN 
            CASE 
                WHEN mr.team1_total_wins < mr.team2_total_wins THEN 'win'
                WHEN mr.team1_total_wins > mr.team2_total_wins THEN 'loss'
                ELSE 'draw'
            END
    END AS match_result,
    CASE 
        WHEN pt.last_round_team = 'team1' THEN mr.team1_total_wins
        WHEN pt.last_round_team = 'team2' THEN mr.team2_total_wins
    END AS score_for,
    CASE 
        WHEN pt.last_round_team = 'team1' THEN mr.team2_total_wins
        WHEN pt.last_round_team = 'team2' THEN mr.team1_total_wins
    END AS score_against,
    mr.match_date,
    mr.team1_wins_as_T,
    team1_wins_as_CT,
    team2_wins_as_CT,
    team2_wins_as_T,
    total_T_wins,
    total_CT_wins
FROM 
    (
    SELECT 
        p.match_id,
        p.steamID,
        MAX(CASE WHEN p.round <= 7 AND p.team = 2 THEN 'team1' WHEN p.round > 7 AND p.team = 3 THEN 'team1' ELSE 'team2' END) AS last_round_team,
        SUM(CASE WHEN p.round <= 7 AND p.team = 2 THEN 1 WHEN p.round > 7 AND p.team = 3 THEN 1 ELSE 0 END) AS rounds_on_team1,
        SUM(CASE WHEN p.round <= 7 AND p.team = 3 THEN 1 WHEN p.round > 7 AND p.team = 2 THEN 1 ELSE 0 END) AS rounds_on_team2
    FROM 
        PLAYER_ROUND_STATS p
    GROUP BY 
        p.match_id,
        p.steamID
    ) pt
LEFT JOIN 
    MATCH_RESULTS mr ON pt.match_id = mr.match_id
);

--select userName, SUM(CASE WHEN match_result='win' THEN 1 ELSE 0 END) as wins, SUM(CASE WHEN match_result='loss' THEN 1 ELSE 0 END) as loss from player_match_results group by(userName) order by SUM(CASE
--WHEN match_result='win' THEN 1 ELSE 0 END) desc;

CREATE OR REPLACE VIEW PLAYER_MATCH_STATS_EXTENDED AS
SELECT
    pms.*,
    mr.last_round_team,
    rounds_on_team1,
    rounds_on_team2,
    ROUND(pms.kills*1.0/pms.roundsPlayed,2) as kpr,
    ROUND(pms.deaths*1.0/pms.roundsPlayed,2) as dpr,
    ROUND(pms.tdh*1.0/pms.roundsPlayed,2) as adr,
    ROUND((
        CAST(pms.kills AS DECIMAL) / CAST(pms.roundsPlayed AS DECIMAL) / 0.679
        + 0.7 * (CAST(pms.roundsPlayed AS DECIMAL) - CAST(pms.deaths AS DECIMAL)) / CAST(pms.roundsPlayed AS DECIMAL) / 0.317
        + (CAST(pms._1k AS DECIMAL) + 4 * CAST(pms._2k AS DECIMAL) + 9 * CAST(pms._3k AS DECIMAL) + 16 * CAST(pms._4k AS DECIMAL) + 25 * CAST(pms._5k AS DECIMAL)) / CAST(pms.roundsPlayed AS DECIMAL) / 1.277
    ) / 2.7, 3) as hltv_rating,
    ROUND(pms.rwsTotal*1.0/pms.roundsPlayed,2) AS rws,
    ROUND(pms.kastTotal*1.0/pms.roundsPlayed,2) as kast
FROM 
    PLAYER_MATCH_STATS pms
LEFT JOIN PLAYER_MATCH_RESULTS mr ON pms.steamid = mr.steamid AND pms.match_id = mr.match_id
WHERE
    pms.roundsPlayed>0;


CREATE OR REPLACE VIEW ROUND_SHOT_STATS_EXTENDED AS 
SELECT
    steamID,
    round,
    match_id,
    REPLACE(weapon,'weapon_','') AS weapon,
    COALESCE(COUNT(eventType),0) AS shots_fired
    
FROM 
    ROUND_SHOT_EVENTS
WHERE REPLACE(weapon,'weapon_','') not in ('flashbang','hegrenade', 'inferno','molotov','smokegrenade', 'incgrenade', 'knife', 'knife_t')
GROUP BY
    steamID,
    round,
    weapon,
    match_id;

CREATE OR REPLACE VIEW ROUND_HIT_STATS_EXTENDED AS
SELECT
    steamID,
    round,
    REPLACE(weapon,'weapon_','') as weapon,
    COALESCE(COUNT(eventtime),0) AS hits,
    SUM(damageHealth) AS total_damage,
    SUM(CASE WHEN hitGroup = 1 THEN 1 ELSE 0 END) AS headshots,
    SUM(CASE WHEN hitGroup IN (4, 5) THEN 1 ELSE 0 END) AS arm_hits,
    SUM(CASE WHEN hitGroup IN (6, 7) THEN 1 ELSE 0 END) AS leg_hits,
    SUM(CASE WHEN hitGroup = 2 THEN 1 ELSE 0 END) AS chest_hits,
    SUM(CASE WHEN hitGroup = 3 THEN 1 ELSE 0 END) AS stomach_hits,
    match_id
FROM 
    ROUND_HIT_EVENTS_EXTENDED
WHERE REPLACE(weapon,'weapon_','') not in ('flashbang','hegrenade', 'inferno','molotov','smokegrenade', 'incgrenade', 'knife', 'knife_t')
GROUP BY
    steamID,
    round,
    weapon,
    match_id;

CREATE OR REPLACE VIEW MATCH_SHOT_STATS_EXTENDED AS 
SELECT
    steamID,
    match_id,
    REPLACE(weapon,'weapon_','') AS weapon,
    COALESCE(COUNT(eventType),0) AS shots_fired
    
FROM 
    ROUND_SHOT_EVENTS
WHERE REPLACE(weapon,'weapon_','') not in ('flashbang','hegrenade', 'inferno','molotov','smokegrenade', 'incgrenade', 'knife', 'knife_t')
GROUP BY
    steamID,
    weapon,
    match_id;

CREATE OR REPLACE VIEW MATCH_HIT_STATS_EXTENDED AS
SELECT
    steamID,
    REPLACE(weapon,'weapon_','') as weapon,
    COALESCE(COUNT(eventtime),0) AS hits,
    SUM(damageHealth) AS total_damage,
    SUM(CASE WHEN hitGroup = 1 THEN 1 ELSE 0 END) AS headshots,
    SUM(CASE WHEN hitGroup IN (4, 5) THEN 1 ELSE 0 END) AS arm_hits,
    SUM(CASE WHEN hitGroup IN (6, 7) THEN 1 ELSE 0 END) AS leg_hits,
    SUM(CASE WHEN hitGroup = 2 THEN 1 ELSE 0 END) AS chest_hits,
    SUM(CASE WHEN hitGroup = 3 THEN 1 ELSE 0 END) AS stomach_hits,
    match_id
FROM 
    ROUND_HIT_EVENTS_EXTENDED
WHERE REPLACE(weapon,'weapon_','') not in ('flashbang','hegrenade', 'inferno','molotov','smokegrenade', 'incgrenade', 'knife', 'knife_t')
GROUP BY
    steamID,
    weapon,
    match_id;

CREATE OR REPLACE VIEW MAP_SHOT_STATS_EXTENDED AS 
SELECT
    steamID,
    mapName,
    REPLACE(weapon,'weapon_','') AS weapon,
    COALESCE(COUNT(eventType),0) AS shots_fired
    
FROM 
    ROUND_SHOT_EVENTS as r
LEFT JOIN
    MATCH_STATS as m ON r.match_id=m.match_id
WHERE REPLACE(weapon,'weapon_','') not in ('flashbang','hegrenade', 'inferno','molotov','smokegrenade', 'incgrenade', 'knife', 'knife_t')
GROUP BY
    steamID,
    weapon,
    mapName;

CREATE OR REPLACE VIEW MAP_HIT_STATS_EXTENDED AS
SELECT
    steamID,
    mapName,
    REPLACE(weapon,'weapon_','') as weapon,
    COALESCE(COUNT(eventtime),0) AS hits,
    SUM(damageHealth) AS total_damage,
    SUM(CASE WHEN hitGroup = 1 THEN 1 ELSE 0 END) AS headshots,
    SUM(CASE WHEN hitGroup IN (4, 5) THEN 1 ELSE 0 END) AS arm_hits,
    SUM(CASE WHEN hitGroup IN (6, 7) THEN 1 ELSE 0 END) AS leg_hits,
    SUM(CASE WHEN hitGroup = 2 THEN 1 ELSE 0 END) AS chest_hits,
    SUM(CASE WHEN hitGroup = 3 THEN 1 ELSE 0 END) AS stomach_hits
FROM 
    ROUND_HIT_EVENTS_EXTENDED as r
LEFT JOIN
    MATCH_STATS as m ON r.match_id=m.match_id
WHERE REPLACE(weapon,'weapon_','') not in ('flashbang','hegrenade', 'inferno','molotov','smokegrenade', 'incgrenade', 'knife', 'knife_t')
GROUP BY
    steamID,
    weapon,
    mapName;

CREATE OR REPLACE VIEW OVERALL_SHOT_STATS_EXTENDED AS 
SELECT
    steamID,
    REPLACE(weapon,'weapon_','') AS weapon,
    COALESCE(COUNT(eventType),0) AS shots_fired
    
FROM 
    ROUND_SHOT_EVENTS
WHERE REPLACE(weapon,'weapon_','') not in ('flashbang','hegrenade', 'inferno','molotov','smokegrenade', 'incgrenade', 'knife', 'knife_t')
GROUP BY
    steamID,
    weapon;

CREATE OR REPLACE VIEW OVERALL_HIT_STATS_EXTENDED AS
SELECT
    steamID,
    REPLACE(weapon,'weapon_','') as weapon,
    COALESCE(COUNT(eventtime),0) AS hits,
    SUM(damageHealth) AS total_damage,
    SUM(CASE WHEN hitGroup = 1 THEN 1 ELSE 0 END) AS headshots,
    SUM(CASE WHEN hitGroup IN (4, 5) THEN 1 ELSE 0 END) AS arm_hits,
    SUM(CASE WHEN hitGroup IN (6, 7) THEN 1 ELSE 0 END) AS leg_hits,
    SUM(CASE WHEN hitGroup = 2 THEN 1 ELSE 0 END) AS chest_hits,
    SUM(CASE WHEN hitGroup = 3 THEN 1 ELSE 0 END) AS stomach_hits
FROM 
    ROUND_HIT_EVENTS_EXTENDED
WHERE REPLACE(weapon,'weapon_','') not in ('flashbang','hegrenade', 'inferno','molotov','smokegrenade', 'incgrenade', 'knife', 'knife_t')
GROUP BY
    steamID,
    weapon;

CREATE OR REPLACE VIEW ROUND_PLAYER_WEAPON_STATS AS
SELECT
    p.steamID,
    p.match_id,
    p.userName,
    s.round,
    s.weapon,
    s.shots_fired,
    h.hits,
    h.total_damage,
    ROUND((h.hits * 100.0 / NULLIF(s.shots_fired, 0)), 2) AS accuracy,
    ROUND((h.total_damage / NULLIF(s.shots_fired, 0)), 2) AS damage_per_shot,
    ROUND((h.total_damage / NULLIF(h.hits, 0)), 2) AS damage_per_hit,
    ROUND((h.headshots * 100.0 / NULLIF(h.hits, 0)), 2) AS headshot_percentage,
    ROUND((h.arm_hits * 100.0 / NULLIF(h.hits, 0)), 2) AS arm_hit_percentage,
    ROUND((h.leg_hits * 100.0 / NULLIF(h.hits, 0)), 2) AS leg_hit_percentage,
    ROUND((h.chest_hits * 100.0 / NULLIF(h.hits, 0)), 2) AS chest_hit_percentage,
    ROUND((h.stomach_hits * 100.0 / NULLIF(h.hits, 0)), 2) AS stomach_hit_percentage
FROM 
    PLAYER_STATS AS p
LEFT JOIN
    ROUND_SHOT_STATS_EXTENDED AS s ON p.steamID = s.steamID AND p.match_id = s.match_id
LEFT JOIN
    ROUND_HIT_STATS_EXTENDED AS h ON p.steamID = h.steamID AND p.match_id = h.match_id AND s.weapon = h.weapon AND s.round = h.round;

CREATE OR REPLACE VIEW MATCH_PLAYER_WEAPON_STATS AS
SELECT
    p.steamID,
    p.match_id,
    p.userName,
    s.weapon,
    s.shots_fired,
    COALESCE(h.hits, 0) as hits,
    COALESCE(k.kills, 0) as kills,
    COALESCE(k.headshots, 0) as headshotkills,
    COALESCE(ROUND(CAST(k.headshots AS NUMERIC) * 100.0 / NULLIF(CAST(k.kills AS NUMERIC), 0), 2), 0) as headshotkills_percentage,
    COALESCE(h.total_damage, 0) as total_damage,
    COALESCE(ROUND((CAST(h.hits AS NUMERIC) * 100.0 / NULLIF(CAST(s.shots_fired AS NUMERIC), 0)), 2), 0) AS accuracy,
    COALESCE(ROUND((CAST(h.total_damage AS NUMERIC) / NULLIF(CAST(s.shots_fired AS NUMERIC), 0)), 2), 0) AS damage_per_shot,
    COALESCE(ROUND((CAST(h.total_damage AS NUMERIC) / NULLIF(CAST(h.hits AS NUMERIC), 0)), 2), 0) AS damage_per_hit,
    COALESCE(ROUND((CAST(h.headshots AS NUMERIC) * 100.0 / NULLIF(CAST(h.hits AS NUMERIC), 0)), 2), 0) AS headshot_percentage,
    COALESCE(ROUND((CAST(h.arm_hits AS NUMERIC) * 100.0 / NULLIF(CAST(h.hits AS NUMERIC), 0)), 2), 0) AS arm_hit_percentage,
    COALESCE(ROUND((CAST(h.leg_hits AS NUMERIC) * 100.0 / NULLIF(CAST(h.hits AS NUMERIC), 0)), 2), 0) AS leg_hit_percentage,
    COALESCE(ROUND((CAST(h.chest_hits AS NUMERIC) * 100.0 / NULLIF(CAST(h.hits AS NUMERIC), 0)), 2), 0) AS chest_hit_percentage,
    COALESCE(ROUND((CAST(h.stomach_hits AS NUMERIC) * 100.0 / NULLIF(CAST(h.hits AS NUMERIC), 0)), 2), 0) AS stomach_hit_percentage
FROM 
    PLAYER_STATS AS p
LEFT JOIN
    MATCH_SHOT_STATS_EXTENDED AS s ON p.steamID = s.steamID AND p.match_id = s.match_id
LEFT JOIN
    MATCH_HIT_STATS_EXTENDED AS h ON p.steamID = h.steamID AND p.match_id = h.match_id AND s.weapon = h.weapon
LEFT JOIN
    PLAYER_WEAPON_MATCH_KILLS AS k ON p.steamID = k.steamID AND p.match_id = k.match_id AND s.weapon = k.weapon;


CREATE OR REPLACE VIEW OVERALL_PLAYER_WEAPON_STATS AS
SELECT DISTINCT ON (p.steamID, s.weapon)
    p.steamID,
    s.weapon,
    s.shots_fired,
    COALESCE(h.hits, 0) as hits,
    COALESCE(k.kills, 0) as kills,
    COALESCE(k.headshots, 0) as headshotkills,
    COALESCE(ROUND(CAST(k.headshots AS NUMERIC) * 100.0 / NULLIF(CAST(k.kills AS NUMERIC), 0), 2), 0) as headshotkills_percentage,
    COALESCE(h.total_damage, 0) as total_damage,
    COALESCE(ROUND((CAST(h.hits AS NUMERIC) * 100.0 / NULLIF(CAST(s.shots_fired AS NUMERIC), 0)), 2), 0) AS accuracy,
    COALESCE(ROUND((CAST(h.total_damage AS NUMERIC) / NULLIF(CAST(s.shots_fired AS NUMERIC), 0)), 2), 0) AS damage_per_shot,
    COALESCE(ROUND((CAST(h.total_damage AS NUMERIC) / NULLIF(CAST(h.hits AS NUMERIC), 0)), 2), 0) AS damage_per_hit,
    COALESCE(ROUND((CAST(h.headshots AS NUMERIC) * 100.0 / NULLIF(CAST(h.hits AS NUMERIC), 0)), 2), 0) AS headshot_percentage,
    COALESCE(ROUND((CAST(h.arm_hits AS NUMERIC) * 100.0 / NULLIF(CAST(h.hits AS NUMERIC), 0)), 2), 0) AS arm_hit_percentage,
    COALESCE(ROUND((CAST(h.leg_hits AS NUMERIC) * 100.0 / NULLIF(CAST(h.hits AS NUMERIC), 0)), 2), 0) AS leg_hit_percentage,
    COALESCE(ROUND((CAST(h.chest_hits AS NUMERIC) * 100.0 / NULLIF(CAST(h.hits AS NUMERIC), 0)), 2), 0) AS chest_hit_percentage,
    COALESCE(ROUND((CAST(h.stomach_hits AS NUMERIC) * 100.0 / NULLIF(CAST(h.hits AS NUMERIC), 0)), 2), 0) AS stomach_hit_percentage
FROM 
    PLAYER_STATS AS p
LEFT JOIN
    OVERALL_SHOT_STATS_EXTENDED AS s ON p.steamID = s.steamID 
LEFT JOIN
    OVERALL_HIT_STATS_EXTENDED AS h ON p.steamID = h.steamID AND s.weapon = h.weapon
LEFT JOIN
    PLAYER_WEAPON_OVERALL_KILLS AS k ON p.steamID = k.steamID AND s.weapon = k.weapon;

---toupdate
CREATE OR REPLACE VIEW MAP_PLAYER_WEAPON_STATS AS
SELECT DISTINCT ON (p.steamID, s.weapon, s.mapName)
    p.steamID,
    s.mapName,
    s.weapon,
    s.shots_fired,
    COALESCE(h.hits, 0) as hits,
    COALESCE(k.kills, 0) as kills,
    COALESCE(k.headshots, 0) as headshotkills,
    COALESCE(ROUND(CAST(k.headshots AS NUMERIC) * 100.0 / NULLIF(CAST(k.kills AS NUMERIC), 0), 2), 0) as headshotkills_percentage,
    COALESCE(h.total_damage, 0) as total_damage,
    COALESCE(ROUND((CAST(h.hits AS NUMERIC) * 100.0 / NULLIF(CAST(s.shots_fired AS NUMERIC), 0)), 2), 0) AS accuracy,
    COALESCE(ROUND((CAST(h.total_damage AS NUMERIC) / NULLIF(CAST(s.shots_fired AS NUMERIC), 0)), 2), 0) AS damage_per_shot,
    COALESCE(ROUND((CAST(h.total_damage AS NUMERIC) / NULLIF(CAST(h.hits AS NUMERIC), 0)), 2), 0) AS damage_per_hit,
    COALESCE(ROUND((CAST(h.headshots AS NUMERIC) * 100.0 / NULLIF(CAST(h.hits AS NUMERIC), 0)), 2), 0) AS headshot_percentage,
    COALESCE(ROUND((CAST(h.arm_hits AS NUMERIC) * 100.0 / NULLIF(CAST(h.hits AS NUMERIC), 0)), 2), 0) AS arm_hit_percentage,
    COALESCE(ROUND((CAST(h.leg_hits AS NUMERIC) * 100.0 / NULLIF(CAST(h.hits AS NUMERIC), 0)), 2), 0) AS leg_hit_percentage,
    COALESCE(ROUND((CAST(h.chest_hits AS NUMERIC) * 100.0 / NULLIF(CAST(h.hits AS NUMERIC), 0)), 2), 0) AS chest_hit_percentage,
    COALESCE(ROUND((CAST(h.stomach_hits AS NUMERIC) * 100.0 / NULLIF(CAST(h.hits AS NUMERIC), 0)), 2), 0) AS stomach_hit_percentage
FROM 
    PLAYER_STATS AS p
LEFT JOIN
    MAP_SHOT_STATS_EXTENDED AS s ON p.steamID = s.steamID 
LEFT JOIN
    MAP_HIT_STATS_EXTENDED AS h ON p.steamID = h.steamID AND s.weapon = h.weapon AND s.mapName = h.mapName
LEFT JOIN
    PLAYER_WEAPON_MAP_KILLS AS k ON p.steamID = k.steamID AND s.weapon = k.weapon AND s.mapName = k.mapName;

CREATE OR REPLACE VIEW ROUND_SCORECARD AS
SELECT 
    p.match_id, 
    p.round, 
    p.team, 
    p.player_count, 
    COALESCE(d.death_count, 0) as death_count, 
    r.winner_team, 
    COALESCE(m.total_money_spent, 0) as total_money_spent, 
    COALESCE(m.total_equipment_value, 0) as total_equipment_value, 
    re.round_end_reason, 
    CASE 
        WHEN p.round = 1 OR p.round = 8 THEN 'pistol'
        WHEN (COALESCE(m.total_money_spent, 0) + COALESCE(m.total_equipment_value, 0)) / p.player_count < 1500 THEN 'eco'
        WHEN (COALESCE(m.total_money_spent, 0) + COALESCE(m.total_equipment_value, 0)) / p.player_count BETWEEN 1500 AND 4000 THEN 'force buy'
        ELSE 'full buy'
    END as round_type,
    cs.t_score, 
    cs.ct_score,
    cs.team1_score, 
    cs.team2_score
FROM 
    (SELECT match_id, round, team, COUNT(*) as player_count
    FROM PLAYER_ROUND_STATS
    GROUP BY match_id, round, team) p
LEFT JOIN 
    (SELECT match_id, round, team, COUNT(*) as death_count
    FROM PLAYER_ROUND_STATS
    WHERE survived = FALSE
    GROUP BY match_id, round, team) d ON p.match_id = d.match_id AND p.round = d.round AND p.team = d.team
LEFT JOIN 
    (SELECT match_id, roundNumber as round, winnerSide as winner_team
    FROM ROUND_STATS) r ON p.match_id = r.match_id AND p.round = r.round
LEFT JOIN 
    (SELECT match_id, round, team, SUM(moneySpent) as total_money_spent, SUM(equipmentValue) as total_equipment_value
    FROM PLAYER_ROUND_STATS
    GROUP BY match_id, round, team) m ON p.match_id = m.match_id AND p.round = m.round AND p.team = m.team
LEFT JOIN 
    (SELECT match_id, roundNumber as round, reasonEndRound as round_end_reason
    FROM ROUND_STATS) re ON p.match_id = re.match_id AND p.round = re.round
JOIN 
    (SELECT 
        rw.match_id, 
        rw.round, 
        SUM(CASE WHEN rw.winner_team = 2 THEN 1 ELSE 0 END) OVER (PARTITION BY rw.match_id ORDER BY rw.round) as t_score,
        SUM(CASE WHEN rw.winner_team = 3 THEN 1 ELSE 0 END) OVER (PARTITION BY rw.match_id ORDER BY rw.round) as ct_score,
        SUM(CASE WHEN (rw.round < 8 AND rw.winner_team = 2) OR ( rw.round >= 8 AND rw.winner_team = 3 ) THEN 1 ELSE 0 END) OVER (PARTITION BY rw.match_id ORDER BY rw.round) as team1_score,
        SUM(CASE WHEN (rw.round < 8 AND rw.winner_team = 3) OR ( rw.round >= 8 AND rw.winner_team = 2 ) THEN 1 ELSE 0 END) OVER (PARTITION BY rw.match_id ORDER BY rw.round) as team2_score
    FROM 
        (SELECT match_id, roundNumber as round, winnerSide as winner_team
        FROM ROUND_STATS) rw) cs ON p.match_id = cs.match_id AND p.round = cs.round;


CREATE OR REPLACE VIEW PLAYER_OVERALL_STATS_EXTENDED AS
SELECT
    pos.steamID,
    pos.usernames,
    COUNT(distinct match_id) matches,
    SUM(pos.roundsPlayed) rounds,
    SUM(pos.kills) as kills,
    SUM(pos.assists) as assists,
    SUM(pos.deaths) as deaths,
    SUM(pos.headshots) as headshots,
    CASE WHEN SUM(pos.deaths)>0 THEN ROUND(SUM(CAST(pos.kills AS DECIMAL))*1.0/SUM(CAST(pos.deaths AS DECIMAL)),2) ELSE 0 END as kdr,
    CASE WHEN SUM(pos.kills)>0 THEN ROUND(SUM(CAST(pos.headshots AS DECIMAL))*100.0/SUM(CAST(pos.kills AS DECIMAL)),2) ELSE 0 END as headshot_percentage,
    SUM(pos.ff) as ff,
    SUM(pos.ek) as ek,
    SUM(pos.bp) as bp,
    SUM(pos.bd) as bd,
    SUM(pos.hr) as hr,
    SUM(pos.mvp) as mvp,
    SUM(pos._5k) as _5k,
    SUM(pos._4k) as _4k,
    SUM(pos._3k) as _3k,
    SUM(pos._2k) as _2k,
    SUM(pos._1k) as _1k,
    SUM(pos.tk) as tk,
    SUM(pos.td) as td,
    SUM(pos.tdh) as tdh,
    SUM(pos.tda) as tda,
    SUM(pos.ffd) as ffd,
    ROUND(AVG(pos.ebt),2) as ebt,
    ROUND(AVG(pos.fbt),2) as fbt,
    ROUND(AVG(pos.ud),2) as ud,
    SUM(pos._1v1) as _1v1,
    SUM(pos._1v2) as _1v2,
    SUM(pos._1v3) as _1v3,
    SUM(pos._1v4) as _1v4,
    SUM(pos._1v5) as _1v5,
    SUM(pos.fa) as fa,
    CASE WHEN SUM(pos.roundsPlayed)>0 THEN ROUND(SUM(CAST(pos.kills AS DECIMAL))*1.0/SUM(CAST(pos.roundsPlayed AS DECIMAL)),2) ELSE 0 END as kpr,
    CASE WHEN SUM(pos.roundsPlayed)>0 THEN ROUND(SUM(CAST(pos.deaths AS DECIMAL))*1.0/SUM(CAST(pos.roundsPlayed AS DECIMAL)),2) ELSE 0 END as dpr,
    CASE WHEN SUM(pos.roundsPlayed)>0 THEN ROUND(SUM(CAST(pos.tdh AS DECIMAL))*1.0/SUM(CAST(pos.roundsPlayed AS DECIMAL)),2) ELSE 0 END as adr,
    ROUND(AVG(hltv_rating),2) as hltv_rating,
    ROUND(AVG(rws),2) AS rws,
    ROUND(AVG(pos.kast),2) as kast
FROM 
    PLAYER_MATCH_STATS_EXTENDED as pos
WHERE
    pos.roundsPlayed > 0
GROUP BY
    pos.steamID;


CREATE OR REPLACE VIEW PLAYER_OVERALL_MATCH_STATS AS
SELECT
    steamID,
    SUM(CASE WHEN match_result='win' THEN 1 ELSE 0 END) as wins, 
    SUM(CASE WHEN match_result='loss' THEN 1 ELSE 0 END) as loss,
        CASE WHEN SUM(CASE WHEN match_result='loss' THEN 1 ELSE 0 END)>0 THEN 
        ROUND(CAST(SUM(CASE WHEN match_result='win' THEN 1 ELSE 0 END) AS DECIMAL)/CAST(SUM(CASE WHEN match_result='loss' THEN 1 ELSE 0 END) AS DECIMAL),2)
        ELSE 0 END as winlossratio,
    ROUND(AVG(score_for)-AVG(score_against),2) as averagewinscore
FROM PLAYER_MATCH_RESULTS 
GROUP BY
    steamID;


CREATE OR REPLACE VIEW PLAYER_WEAPON_RANKING AS
SELECT
    steamID,
    weapon,
    ROW_NUMBER() OVER (PARTITION BY steamID ORDER BY kills DESC) as weapon_rank
FROM 
    PLAYER_WEAPON_OVERALL_KILLS;

CREATE OR REPLACE VIEW PLAYER_MAP_STATS_EXTENDED AS
SELECT
    pos.steamID,
    m.mapName,
    COUNT(distinct m.match_id) matches,
    SUM(pos.roundsPlayed) rounds,
    SUM(pos.kills) as kills,
    SUM(pos.assists) as assists,
    SUM(pos.deaths) as deaths,
    SUM(pos.headshots) as headshots,
    CASE WHEN SUM(pos.deaths)>0 THEN ROUND(SUM(CAST(pos.kills AS DECIMAL))*1.0/SUM(CAST(pos.deaths AS DECIMAL)),2) ELSE 0 END as kdr,
    CASE WHEN SUM(pos.kills)>0 THEN ROUND(SUM(CAST(pos.headshots AS DECIMAL))*100.0/SUM(CAST(pos.kills AS DECIMAL)),2) ELSE 0 END as headshot_percentage,
    SUM(pos.ff) as ff,
    SUM(pos.ek) as ek,
    SUM(pos.bp) as bp,
    SUM(pos.bd) as bd,
    SUM(pos.hr) as hr,
    SUM(pos.mvp) as mvp,
    SUM(pos._5k) as _5k,
    SUM(pos._4k) as _4k,
    SUM(pos._3k) as _3k,
    SUM(pos._2k) as _2k,
    SUM(pos._1k) as _1k,
    SUM(pos.tk) as tk,
    SUM(pos.td) as td,
    SUM(pos.tdh) as tdh,
    SUM(pos.tda) as tda,
    SUM(pos.ffd) as ffd,
    ROUND(AVG(pos.ebt),2) as ebt,
    ROUND(AVG(pos.fbt),2) as fbt,
    ROUND(AVG(pos.ud),2) as ud,
    SUM(pos._1v1) as _1v1,
    SUM(pos._1v2) as _1v2,
    SUM(pos._1v3) as _1v3,
    SUM(pos._1v4) as _1v4,
    SUM(pos._1v5) as _1v5,
    SUM(pos.fa) as fa,
    CASE WHEN SUM(pos.roundsPlayed)>0 THEN ROUND(SUM(CAST(pos.kills AS DECIMAL))*1.0/SUM(CAST(pos.roundsPlayed AS DECIMAL)),2) ELSE 0 END as kpr,
    CASE WHEN SUM(pos.roundsPlayed)>0 THEN ROUND(SUM(CAST(pos.deaths AS DECIMAL))*1.0/SUM(CAST(pos.roundsPlayed AS DECIMAL)),2) ELSE 0 END as dpr,
    CASE WHEN SUM(pos.roundsPlayed)>0 THEN ROUND(SUM(CAST(pos.tdh AS DECIMAL))*1.0/SUM(CAST(pos.roundsPlayed AS DECIMAL)),2) ELSE 0 END as adr,
    ROUND(AVG(hltv_rating),2) as hltv_rating,
    ROUND(AVG(rws),2) AS rws,
    ROUND(AVG(pos.kast),2) as kast
FROM 
    PLAYER_MATCH_STATS_EXTENDED as pos
LEFT JOIN
    MATCH_STATS as m ON pos.match_id=m.match_id
WHERE
    pos.roundsPlayed > 0
GROUP BY
    m.mapName,
    pos.steamID;


CREATE OR REPLACE VIEW PLAYER_MAP_MATCH_STATS AS
SELECT
    steamID,
    m.mapName,
    SUM(CASE WHEN match_result='win' THEN 1 ELSE 0 END) as wins, 
    SUM(CASE WHEN match_result='loss' THEN 1 ELSE 0 END) as loss,
    CASE WHEN SUM(CASE WHEN match_result='loss' THEN 1 ELSE 0 END)>0 THEN 
        ROUND(CAST(SUM(CASE WHEN match_result='win' THEN 1 ELSE 0 END) AS DECIMAL)/CAST(SUM(CASE WHEN match_result='loss' THEN 1 ELSE 0 END) AS DECIMAL),2)
        ELSE 0 END as winlossratio,
    ROUND(AVG(score_for)-AVG(score_against),2) as averagewinscore
FROM PLAYER_MATCH_RESULTS as r
LEFT JOIN
    MATCH_STATS as m ON r.match_id=m.match_id
GROUP BY
    m.mapName,
    steamID;

CREATE OR REPLACE VIEW PLAYER_WEAPON_MAP_RANKING AS
SELECT
    steamID,
    mapName,
    weapon,
    ROW_NUMBER() OVER (PARTITION BY steamID, mapName ORDER BY kills DESC) as weapon_rank
FROM 
    PLAYER_WEAPON_MAP_KILLS;


CREATE OR REPLACE VIEW PLAYER_MAP_STATS_EXTENDED_EXTENDED AS
SELECT
    o.*,
    m.wins,
    m.loss,
    m.winlossratio,
    m.averagewinscore,
    w1.weapon AS first_weapon,
    w2.weapon AS second_weapon
FROM
    PLAYER_MAP_STATS_EXTENDED o LEFT JOIN PLAYER_MAP_MATCH_STATS AS m ON o.steamid=m.steamid AND o.mapName=m.mapName
LEFT JOIN
    PLAYER_WEAPON_MAP_RANKING w1 ON o.steamid=w1.steamid AND o.mapName=w1.mapName AND w1.weapon_rank = 1
LEFT JOIN
    PLAYER_WEAPON_MAP_RANKING w2 ON o.steamid=w2.steamid AND o.mapName=w2.mapName AND w2.weapon_rank = 2;


CREATE OR REPLACE VIEW PLAYER_CLUTCH_STATS AS
SELECT 
    steamID, 
    CASE WHEN SUM(CASE WHEN clutchChance = 1 THEN 1 ELSE 0 END) = 0 THEN 0
         ELSE ROUND(SUM(CASE WHEN clutchChance = 1 AND clutchSuccess THEN 1 ELSE 0 END) * 100.0 / SUM(CASE WHEN clutchChance = 1 THEN 1 ELSE 0 END), 2)
    END AS _1v1p,
    SUM(CASE WHEN clutchChance = 1 AND clutchSuccess THEN 1 ELSE 0 END) AS _1v1w,
    SUM(CASE WHEN clutchChance = 1 AND NOT clutchSuccess THEN 1 ELSE 0 END) AS _1v1l,
    CASE WHEN SUM(CASE WHEN clutchChance = 2 THEN 1 ELSE 0 END) = 0 THEN 0
         ELSE ROUND(SUM(CASE WHEN clutchChance = 2 AND clutchSuccess THEN 1 ELSE 0 END) * 100.0 / SUM(CASE WHEN clutchChance = 2 THEN 1 ELSE 0 END), 2)
    END AS _1v2p,
    SUM(CASE WHEN clutchChance = 2 AND clutchSuccess THEN 1 ELSE 0 END) AS _1v2w,
    SUM(CASE WHEN clutchChance = 2 AND NOT clutchSuccess THEN 1 ELSE 0 END) AS _1v2l,
    CASE WHEN SUM(CASE WHEN clutchChance = 3 THEN 1 ELSE 0 END) = 0 THEN 0
         ELSE ROUND(SUM(CASE WHEN clutchChance = 3 AND clutchSuccess THEN 1 ELSE 0 END) * 100.0 / SUM(CASE WHEN clutchChance = 3 THEN 1 ELSE 0 END), 2)
    END AS _1v3p,
    SUM(CASE WHEN clutchChance = 3 AND clutchSuccess THEN 1 ELSE 0 END) AS _1v3w,
    SUM(CASE WHEN clutchChance = 3 AND NOT clutchSuccess THEN 1 ELSE 0 END) AS _1v3l,
    CASE WHEN SUM(CASE WHEN clutchChance = 4 THEN 1 ELSE 0 END) = 0 THEN 0
         ELSE ROUND(SUM(CASE WHEN clutchChance = 4 AND clutchSuccess THEN 1 ELSE 0 END) * 100.0 / SUM(CASE WHEN clutchChance = 4 THEN 1 ELSE 0 END), 2)
    END AS _1v4p,
    SUM(CASE WHEN clutchChance = 4 AND clutchSuccess THEN 1 ELSE 0 END) AS _1v4w,
    SUM(CASE WHEN clutchChance = 4 AND NOT clutchSuccess THEN 1 ELSE 0 END) AS _1v4l,
    CASE WHEN SUM(CASE WHEN clutchChance = 5 THEN 1 ELSE 0 END) = 0 THEN 0
         ELSE ROUND(SUM(CASE WHEN clutchChance = 5 AND clutchSuccess THEN 1 ELSE 0 END) * 100.0 / SUM(CASE WHEN clutchChance = 5 THEN 1 ELSE 0 END), 2)
    END AS _1v5p,
    SUM(CASE WHEN clutchChance >= 5 AND clutchSuccess THEN 1 ELSE 0 END) AS _1v5w,
    SUM(CASE WHEN clutchChance >= 5 AND NOT clutchSuccess THEN 1 ELSE 0 END) AS _1v5l,
    CASE WHEN SUM(CASE WHEN clutchChance > 0 THEN 1 ELSE 0 END) = 0 THEN 0
         ELSE ROUND(SUM(CASE WHEN clutchSuccess THEN 1 ELSE 0 END) * 100.0 / SUM(CASE WHEN clutchChance > 0 THEN 1 ELSE 0 END), 2)
    END AS _1vNp,
    SUM(CASE WHEN clutchSuccess THEN 1 ELSE 0 END) AS _1vNw,
    SUM(CASE WHEN NOT clutchSuccess AND clutchChance > 0 THEN 1 ELSE 0 END) AS _1vNl
FROM PLAYER_ROUND_STATS
GROUP BY steamID;

CREATE OR REPLACE VIEW PLAYER_OVERALL_STATS_EXTENDED_EXTENDED AS
SELECT
    o.*,
    m.wins,
    m.loss,
    m.winlossratio,
    m.averagewinscore,
    c._1vNp,
    e.ek_success_rate_overall as fkr,
    w1.weapon AS first_weapon,
    w2.weapon AS second_weapon
FROM
    PLAYER_OVERALL_STATS_EXTENDED o 
LEFT JOIN 
    PLAYER_OVERALL_MATCH_STATS AS m ON o.steamid=m.steamid
LEFT JOIN 
    PLAYER_CLUTCH_STATS AS c ON o.steamid=c.steamid
LEFT JOIN
    ENTRY_KILL_STATS_EXTENDED AS e ON o.steamid=e.steamid
LEFT JOIN
    PLAYER_WEAPON_RANKING w1 ON o.steamid=w1.steamid AND w1.weapon_rank = 1
LEFT JOIN
    PLAYER_WEAPON_RANKING w2 ON o.steamid=w2.steamid AND w2.weapon_rank = 2;