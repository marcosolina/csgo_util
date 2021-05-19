/*
 * Terminating all the connections
 */

SELECT pg_terminate_backend(pg_stat_activity.pid) FROM pg_stat_activity WHERE pg_stat_activity.datname = 'csgorcon';

DROP DATABASE IF EXISTS CSGORCON;

CREATE DATABASE CSGORCON;

/*
 * Select the database
 */
\c csgorcon;

/*
 * Create the users table
 */
CREATE TABLE EVENT_LISTENERS (
    URL_LISTENER           VARCHAR(1000)        DEFAULT ''	NOT NULL,
    EVENT_TYPE             VARCHAR(20)  		DEFAULT ''	NOT NULL,
    LAST_SUCCESSFUL        TIMESTAMP,
    LAST_FAILURE           TIMESTAMP,
    CONSECUTIVE_FAILURE    INTEGER              DEFAULT 0   NOT NULL,
    ACTIVE                 VARCHAR(1)           DEFAULT ''  NOT NULL,
    PRIMARY KEY(URL_LISTENER, EVENT_TYPE)
);

INSERT INTO EVENT_LISTENERS VALUES
('http://192.168.1.26:8763/zuul/ixigo-discord-bot/discordbot/csgoevent', 'CS_WIN_PANEL_MATCH', NULL, NULL, 0, 'Y'),
('http://192.168.1.26:8763/zuul/ixigo-discord-bot/discordbot/csgoevent', 'WARMUP_END', NULL, NULL, 0, 'Y'),
('http://192.168.1.26:8763/zuul/ixigo-discord-bot/discordbot/csgoevent', 'WARMUP_START', NULL, NULL, 0, 'Y'),
('http://192.168.1.26:8763/zuul/ixigo-server-helper/ixigohelper/event', 'WARMUP_START', NULL, NULL, 0, 'Y'),
('http://192.168.1.26:8763/zuul/ixigo-server-helper/ixigohelper/event', 'SHUT_DOWN', NULL, NULL, 0, 'Y');

