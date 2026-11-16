/*
 * Terminating all the connections
 */

SELECT pg_terminate_backend(pg_stat_activity.pid) FROM pg_stat_activity WHERE pg_stat_activity.datname = 'event_dispatcher';

DROP DATABASE IF EXISTS EVENT_DISPATCHER;

CREATE DATABASE EVENT_DISPATCHER;

/*
 * Select the database
 */
\c event_dispatcher;

/*
 * Create the table used to register the event listeners.
 * This table will be used when an event needs to be dispatched.
 */
CREATE TABLE EVENT_LISTENERS (
    URL_LISTENER           VARCHAR(1000)        DEFAULT ''	NOT NULL, -- Where to send the event
    EVENT_TYPE             VARCHAR(50)  		DEFAULT ''	NOT NULL, -- The type of event to dispatch
    LAST_SUCCESSFUL        TIMESTAMP,                                 -- The last time that this service was able to dispatch the event
    LAST_FAILURE           TIMESTAMP,                                 -- The last time that this service was NOT able to dispatch the event
    CONSECUTIVE_FAILURE    INTEGER              DEFAULT 0   NOT NULL, -- Number of consecutive failed attempts to dispatch the event
    ACTIVE                 VARCHAR(1)           DEFAULT ''  NOT NULL, -- Status of the listener (Y/N). It will be set to N after X consecutive failures
    PRIMARY KEY(URL_LISTENER, EVENT_TYPE)
);

/*
 * Inserting default values. Every listener is reached through the public gateway; set
 * IXIGO_LISTENERS_BASE_URL on the postgres container to use another one
 * (031-setEventListenersBaseUrl.sh rewrites the base after this script).
 */
INSERT INTO EVENT_LISTENERS VALUES
('https://marco.selfip.net/ixigoproxy/ixigo-discord-bot/discordbot/events',     'CS_WIN_PANEL_MATCH',      NULL, NULL, 0, 'Y'),
('https://marco.selfip.net/ixigoproxy/ixigo-discord-bot/discordbot/events',     'WARMUP_END',              NULL, NULL, 0, 'Y'),
('https://marco.selfip.net/ixigoproxy/ixigo-discord-bot/discordbot/events',     'WARMUP_START',            NULL, NULL, 0, 'Y'),
('https://marco.selfip.net/ixigoproxy/ixigo-discord-bot/discordbot/events',     'AZ_START_CSGO',           NULL, NULL, 0, 'Y'),
('https://marco.selfip.net/ixigoproxy/ixigo-discord-bot/discordbot/events',     'DEM_FILES_PROCESSED',     NULL, NULL, 0, 'Y'),
('https://marco.selfip.net/ixigoproxy/ixigo-server-helper/ixigohelper/event',   'WARMUP_START',            NULL, NULL, 0, 'Y'),
('https://marco.selfip.net/ixigoproxy/ixigo-server-helper/ixigohelper/event',   'SHUT_DOWN',               NULL, NULL, 0, 'Y'),
('https://marco.selfip.net/ixigoproxy/ixigo-server-helper/ixigohelper/event',   'WARMUP_END',              NULL, NULL, 0, 'Y'),
('https://marco.selfip.net/ixigoproxy/ixigo-notification/notification/events',  'AZ_START_DEPLOY_VM',      NULL, NULL, 0, 'Y'),
('https://marco.selfip.net/ixigoproxy/ixigo-notification/notification/events',  'AZ_START_CONFIGURING_VM', NULL, NULL, 0, 'Y'),
('https://marco.selfip.net/ixigoproxy/ixigo-notification/notification/events',  'AZ_START_INSTALLING_CSGO',NULL, NULL, 0, 'Y'),
('https://marco.selfip.net/ixigoproxy/ixigo-notification/notification/events',  'AZ_START_CSGO',           NULL, NULL, 0, 'Y'),
('https://marco.selfip.net/ixigoproxy/ixigo-notification/notification/events',  'AZ_START_DELETE_RESOURCE',NULL, NULL, 0, 'Y');
