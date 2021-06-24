#!/bin/bash

sleep 30

java -jar IxigoDemManager.jar \
--spring.cloud.config.username=$IXIGO_CONFIG_SERVER_USER \
--spring.cloud.config.password=$IXIGO_CONFIG_SERVER_PASSW 