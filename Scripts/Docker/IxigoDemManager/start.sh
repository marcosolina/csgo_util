#!/bin/bash

# Give some time to the Eureka container and config container to start
sleep 20

java -jar IxigoDemManager.jar \
--spring.cloud.config.uri=$IXIGO_CONFIG_SERVER_URI \
--spring.cloud.config.username=$IXIGO_CONFIG_SERVER_USER \
--spring.cloud.config.password=$IXIGO_CONFIG_SERVER_PASSW \
--spring.datasource.username=$POSTGRES_USER \
--spring.datasource.password=$POSTGRES_PASSWORD 