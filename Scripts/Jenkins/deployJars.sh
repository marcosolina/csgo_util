#!/bin/bash

WORKSPACE_FOLDER=$1
RASP_IP=192.168.1.27
BASE_FOLDER=/opt/ixigo
USR=pi

SSH_ADDRESS=$USR@$RASP_IP

ssh $SSH_ADDRESS mkdir -p $BASE_FOLDER

# Deploy DemParser C#
DEM_APP_FOLDER=$BASE_FOLDER/DemParser
ssh $SSH_ADDRESS mkdir -p $DEM_APP_FOLDER
ssh $SSH_ADDRESS rm -rf $DEM_APP_FOLDER/*

scp -r $WORKSPACE_FOLDER/DemParser/bin/Debug/netcoreapp3.1/linux-arm64/* $SSH_ADDRESS:$DEM_APP_FOLDER
ssh $SSH_ADDRESS chmod +x $DEM_APP_FOLDER

# Deply Java apps
apps=(
"IxigoDiscovery"
"IxigoConfigServer"
"IxigoDemManager"
"IxigoDiscordBot"
"IxigoEventDispatcher"
"IxigoPlayersManager"
"IxigoProxy"
"IxigoRconApi"
"IxigoUi"
)


for i in ${!apps[@]}; do
  APP_NAME=${apps[$i]}
  DEPLOY_APP_FOLDER=$BASE_FOLDER/$APP_NAME

  ssh $SSH_ADDRESS mkdir -p $DEPLOY_APP_FOLDER
  scp $WORKSPACE_FOLDER/$APP_NAME/Scripts/startStop.sh $SSH_ADDRESS:$DEPLOY_APP_FOLDER
  ssh $SSH_ADDRESS chmod +x $DEPLOY_APP_FOLDER/startStop.sh
  ssh $SSH_ADDRESS $DEPLOY_APP_FOLDER/startStop.sh stop
  sleep 20

  scp $WORKSPACE_FOLDER/$APP_NAME/target/$APP_NAME*.jar $SSH_ADDRESS:$DEPLOY_APP_FOLDER/$APP_NAME.jar
  ssh -t -t $SSH_ADDRESS << EOF
  export BASH_ENV=/etc/bash.bashrc && echo "" > $DEPLOY_APP_FOLDER/$APP_NAME.log && $DEPLOY_APP_FOLDER/startStop.sh start && exit
EOF
done




