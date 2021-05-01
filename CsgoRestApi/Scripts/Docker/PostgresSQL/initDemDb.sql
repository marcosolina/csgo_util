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
    LAST_SUCCESSFUL        TIMESTAMP                        NOT NULL,
    LAST_LAST_FAILURE      TIMESTAMP                        NOT NULL,
    CONSECUTIVE_FAILURE    INTEGER              DEFAULT 0   NOT NULL,
    ACTIVE                 VARCHAR(1)           DEFAULT ''  NOT NULL,
    PRIMARY KEY(URL_LISTENER, EVENT_TYPE)
);
