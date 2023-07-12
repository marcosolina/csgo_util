DROP TABLE IF EXISTS round_stats CASCADE;
DROP TABLE IF EXISTS round_shot_events CASCADE;
DROP TABLE IF EXISTS round_kill_events CASCADE;
DROP TABLE IF EXISTS round_hit_events CASCADE;
DROP TABLE IF EXISTS round_events CASCADE;
DROP TABLE IF EXISTS player_stats CASCADE;
DROP TABLE IF EXISTS player_round_stats CASCADE;
DROP TABLE IF EXISTS match_stats CASCADE;

CREATE TABLE MATCH_STATS (
    match_id SERIAL PRIMARY KEY,
    match_date TIMESTAMP,
    mapName VARCHAR(255),
    match_fileName VARCHAR(255)
);

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
    eventtime NUMERIC(6, 2), 
    steamID VARCHAR(255),
    round INTEGER,
    match_id INTEGER,
    FOREIGN KEY (match_id) REFERENCES MATCH_STATS(match_id)
);

CREATE TABLE ROUND_KILL_EVENTS (
    eventtime INTEGER, 
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
    eventtime NUMERIC(6, 2), 
    steamID VARCHAR(255),
    round INTEGER,
    weapon VARCHAR(255),
    match_id INTEGER,
    FOREIGN KEY (match_id) REFERENCES MATCH_STATS(match_id)
);

CREATE TABLE ROUND_HIT_EVENTS (
    eventtime NUMERIC(6, 2), 
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
    ELSE 0 END AS rws
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
    p.userName,
    p.match_id,
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
    SUM(r.rws) as rwsTotal
    
FROM
    PLAYER_STATS p
LEFT JOIN
    PLAYER_ROUND_EXTENDED_STATS r ON p.steamID = r.steamID AND p.match_id = r.match_id
GROUP BY
    p.steamID,
    p.match_id,
    p.score,
    p.userName;


CREATE OR REPLACE VIEW PLAYER_MATCH_STATS_EXTENDED AS
SELECT
    pms.*,
    ROUND((
        CAST(pms.kills AS DECIMAL) / CAST(pms.roundsPlayed AS DECIMAL) / 0.679
        + 0.7 * (CAST(pms.roundsPlayed AS DECIMAL) - CAST(pms.deaths AS DECIMAL)) / CAST(pms.roundsPlayed AS DECIMAL) / 0.317
        + (CAST(pms._1k AS DECIMAL) + 4 * CAST(pms._2k AS DECIMAL) + 9 * CAST(pms._3k AS DECIMAL) + 16 * CAST(pms._4k AS DECIMAL) + 25 * CAST(pms._5k AS DECIMAL)) / CAST(pms.roundsPlayed AS DECIMAL) / 1.277
    ) / 2.7, 3) as hltv_rating,
    ROUND(pms.rwsTotal/pms.roundsPlayed,2) AS rws
FROM 
    PLAYER_MATCH_STATS pms
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
    ROUND_HIT_EVENTS
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
    ROUND_HIT_EVENTS
WHERE REPLACE(weapon,'weapon_','') not in ('flashbang','hegrenade', 'inferno','molotov','smokegrenade', 'incgrenade', 'knife', 'knife_t')
GROUP BY
    steamID,
    weapon,
    match_id;

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
    ROUND_HIT_EVENTS
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
    MATCH_SHOT_STATS_EXTENDED AS s ON p.steamID = s.steamID AND p.match_id = s.match_id
LEFT JOIN
    MATCH_HIT_STATS_EXTENDED AS h ON p.steamID = h.steamID AND p.match_id = h.match_id AND s.weapon = h.weapon;


CREATE OR REPLACE VIEW OVERALL_PLAYER_WEAPON_STATS AS
SELECT DISTINCT ON (p.steamID, s.weapon)
    p.steamID,
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
    OVERALL_SHOT_STATS_EXTENDED AS s ON p.steamID = s.steamID 
LEFT JOIN
    OVERALL_HIT_STATS_EXTENDED AS h ON p.steamID = h.steamID AND s.weapon = h.weapon;