#!/bin/bash

sleep 20

echo "####################"
echo $IXIGO_CONFIG_SERVER_USER
echo $IXIGO_CONFIG_SERVER_PASSW
echo $IXIGO_EUREKA_SERVER
echo $IXIGO_POSTGRES_USER
echo $IXIGO_POSTGRES_PASSW
ping -c 4 config-service
echo "####################"

java -jar IxigoDemManager.jar \
--spring.cloud.config.uri=$IXIGO_CONFIG_SERVER_URI \
--spring.cloud.config.username=$IXIGO_CONFIG_SERVER_USER \
--spring.cloud.config.password=$IXIGO_CONFIG_SERVER_PASSW \
--spring.datasource.username=$IXIGO_POSTGRES_USER \
--spring.datasource.password=$IXIGO_POSTGRES_PASSW 