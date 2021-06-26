#!/bin/bash

sleep 5

echo "####################"
echo $IXIGO_EUREKA_HOST_NAME
echo $IXIGO_EUREKA_SERVER
echo "####################"

java -jar IxigoDiscovery.jar