#!/bin/bash

sleep 60

echo "###########################################"
echo $IXIGO_POSTGRES_PASSW
echo $IXIGO_POSTGRES_USER
echo "###########################################"

java -jar IxigoDemManager.jar \ 
--spring.profiles.active=docker \ 
--spring.cloud.config.uri=http://configService:8888/config \ 
--eureka.client.serviceUrl.defaultZone=http://discoveryService:8765/ixigodiscovery/eureka \ 
--spring.cloud.config.username=$IXIGO_CONFIG_SERVER_USER \ 
--spring.cloud.config.password=$IXIGO_CONFIG_SERVER_PASSW