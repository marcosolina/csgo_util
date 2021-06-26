#!/bin/bash

sleep 35

java -jar IxigoDiscordBot.jar \
--spring.cloud.config.uri=$IXIGO_CONFIG_SERVER_URI \
--spring.cloud.config.username=$IXIGO_CONFIG_SERVER_USER \
--spring.cloud.config.password=$IXIGO_CONFIG_SERVER_PASSW \
--spring.datasource.username=$IXIGO_POSTGRES_USER \
--spring.datasource.password=$IXIGO_POSTGRES_PASSW 