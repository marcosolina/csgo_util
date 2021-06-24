#!/bin/bash

sleep 30

export IXIGO_EUREKA_SERVER=http://discoveryService:8765/ixigodiscovery/eureka
export IXIGO_CONFIG_SERVER_URI=http://configService:8888/config

java -jar IxigoDemManager.jar --spring.profiles.active=docker