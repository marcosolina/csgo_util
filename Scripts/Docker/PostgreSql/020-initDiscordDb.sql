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
CREATE TABLE USERS_MAP (
    DISCORD_ID             BIGINT          DEFAULT 0   NOT NULL PRIMARY KEY,
    STEAM_ID               VARCHAR(100)    DEFAULT ''  NOT NULL,
    DISCORD_NAME           VARCHAR(50)     DEFAULT ''  NOT NULL,
    STEAM_NAME             VARCHAR(50)     DEFAULT ''  NOT NULL
);

/*
 * Misc values stored as config 
 */
CREATE TABLE BOT_CONFIG (
    CONFIG_KEY             VARCHAR(100)    DEFAULT ''  NOT NULL PRIMARY KEY,
    CONFIG_VAL             VARCHAR(100)    DEFAULT ''  NOT NULL,
    CONFIG_VALUE_TYPE      VARCHAR(100)    DEFAULT ''  NOT NULL
);


/*
 * Insert default values
 */
INSERT INTO BOT_CONFIG VALUES 
('MATCHES_TO_CONSIDER_FOR_TEAM_CREATION', '20', 'NUMBER'),
('AUTOBALANCE', 'true', 'BOOLEAN'),
('MOVE_TO_VOICE_CHANNEL', 'true', 'BOOLEAN'),
('KICK_BOTS', 'false', 'BOOLEAN'),
('POST_TEAMS_ON_JOIN_LEFT_VOICE_CHANNEL', 'false', 'BOOLEAN');

INSERT INTO USERS_MAP VALUES
(178536311345381376,  '76561197960993150', 'Buckshot', 'Buckshot'),
(192954158456897536,  '76561199057392192', 'chiCkin', 'chiCkin'),
(290008054303424513,  '76561198093594380', 'Austinio7116', 'S.S.G. Tolkien'),
(295893057969061888,  '76561197961466528', 'eik3000', 'EIK3000'),
(699706247439450224,  '76561198029333008', 'LagathaChristie', 'Lagatha Christie'),
(706122841086492704,  '76561199045993244', 'Lrapero', 'Sir Pollo Loco'),
(706499991103078511,  '76561198908381989', 'jackydubs', 'jackydubs'),
(706788204753322025,  '76561199052006347', 'jonnyjapes', 'jonnyjapes'),
(706941967363211404,  '76561198072484969', 'dannyhangboy', 'dannyhangboy'),
(706942739966722128,  '76561197965589644', 'Marco.', 'Marco'),
(693124809923493909,  '76561197993629987', 'KVandal', 'KV'),
(783019764544962571,  '76561199111430276', 'ahenderson2509', 'ahenderson2509'),
(783032831974572072,  '76561198310186195', 'Queen_Ug', 'Queen_Ug'),
(780460520137687080,  '76561199107771304', 'vks2020', 'vks2020'),
(331938199997906954,  '76561197962351233', 'Tippeh', 'Tippeh'),
(421412080978231326,  '76561197963053225', 'ASquirrelsTail', 'Handbagatha Christie'),
(488047418219167744,  '76561198266269604', 'Xenthy', 'Gloomz'),
(701380450865643610,  '76561197974132960', 'hallchr', 'Wagatha Christie'),
(385428685404110879,  '76561198044148171', 'I Am Murdock', 'I am Murdock'),
(716275419027865732,  '76561198119203032', 'Thomas', 'Chinwagatha Christie'),
(839531296488161329,  '76561199041012759', 'su-dont', 'dr.hammer'),
(930150944530923591,  '76561199039770275', 'Pieter', 'Chipieto'),
(321242411760222209,  '76561197960332990', 'Facet', 'Facet'),
(692072850261278863,  '76561198970656823', 'SimpleBonsai', 'SimpleBonsai'),
(985444304858259528,  '76561199465861599', 'Lolly11luce', 'Lolly11luce'),
(690368030961827841,  '76561199511995652', 'MariaSchuller', 'MariaSchuller'),
(1115305932876959764, '76561199512100675', 'rebeccasolina', 'rebeccasolina'),
(1115304552586018826, '76561199512100675', 'rebeccasolina', 'rebeccasolina'),
(1052002906527772783, '76561199112466556', 'elgringo', 'elgringo');



