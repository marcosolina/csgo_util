#!/bin/bash

WORKSPACE_FOLDER=$1
RASP_IP=192.168.1.24
BASE_FOLDER=/opt/ixigo


ssh pi@$RASP_IP mkdir -p $BASE_FOLDER

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


SSH_ADDRESS=pi@$RASP_IP


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




