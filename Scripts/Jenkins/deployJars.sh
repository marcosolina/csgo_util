#!/bin/bash

WORKSPACE_FOLDER=$1

#RASP_1=rasp4gb.lan
RASP_1=docker.lan
RASP_2=rasp30.lan
RASP_3=ixigoservices.lan

BASE_FOLDER=/opt/ixigo
USR=pi

SSH_ADDRESS_1=marco@$RASP_1
SSH_ADDRESS_2=$USR@$RASP_2
SSH_ADDRESS_3=marco@$RASP_3

ssh $SSH_ADDRESS_1 mkdir -p $BASE_FOLDER
ssh $SSH_ADDRESS_2 mkdir -p $BASE_FOLDER
ssh $SSH_ADDRESS_3 mkdir -p $BASE_FOLDER

# Deploy DemParser C#
#DEM_APP_FOLDER=$BASE_FOLDER/DemParser
#ssh $SSH_ADDRESS_1 mkdir -p $DEM_APP_FOLDER
#ssh $SSH_ADDRESS_1 rm -rf $DEM_APP_FOLDER/*

#scp -r $WORKSPACE_FOLDER/DemParser/bin/Debug/netcoreapp3.1/linux-arm64/* $SSH_ADDRESS_1:$DEM_APP_FOLDER
#ssh $SSH_ADDRESS_1 chmod +x $DEM_APP_FOLDER

# Deploy NodeJS DemParser
DEM_APP_FOLDER=$BASE_FOLDER/DemParserNodeJs
ssh $SSH_ADDRESS_1 mkdir -p $DEM_APP_FOLDER
ssh $SSH_ADDRESS_1 rm -rf $DEM_APP_FOLDER/*
scp -r $WORKSPACE_FOLDER/DemParserNodeJs/* $SSH_ADDRESS_1:$DEM_APP_FOLDER
ssh $SSH_ADDRESS_1 chmod +x $DEM_APP_FOLDER
ssh -t -t $SSH_ADDRESS_1 << EOF
export BASH_ENV=/etc/bash.bashrc && cd $DEM_APP_FOLDER && npm install --save demofile && exit
EOF

# Deploy NodeJS DemParser CS2
DEM_APP_FOLDER=$BASE_FOLDER/DemoParserCS2
ssh $SSH_ADDRESS_1 mkdir -p $DEM_APP_FOLDER
ssh $SSH_ADDRESS_1 rm -rf $DEM_APP_FOLDER/*
scp -r $WORKSPACE_FOLDER/DemoParserCS2/* $SSH_ADDRESS_1:$DEM_APP_FOLDER
ssh $SSH_ADDRESS_1 chmod +x $DEM_APP_FOLDER
ssh -t -t $SSH_ADDRESS_1 << EOF
export BASH_ENV=/etc/bash.bashrc && cd $DEM_APP_FOLDER && npm install && exit
EOF

# Deply Java apps
apps=(
#"IxigoDiscovery"
#"IxigoConfigServer"
"IxigoDemManager"
#"IxigoDiscordBot"
#"IxigoEventDispatcher"
#"IxigoPlayersManager"
#"IxigoProxy"
#"IxigoRconApi"
#"IxigoUi"
)


for i in ${!apps[@]}; do
  APP_NAME=${apps[$i]}
  DEPLOY_APP_FOLDER=$BASE_FOLDER/$APP_NAME

  ssh $SSH_ADDRESS_1 mkdir -p $DEPLOY_APP_FOLDER
  scp $WORKSPACE_FOLDER/$APP_NAME/Scripts/startStop.sh $SSH_ADDRESS_1:$DEPLOY_APP_FOLDER
  ssh $SSH_ADDRESS_1 chmod +x $DEPLOY_APP_FOLDER/startStop.sh
  ssh $SSH_ADDRESS_1 $DEPLOY_APP_FOLDER/startStop.sh stop
  sleep 5

  scp $WORKSPACE_FOLDER/$APP_NAME/target/$APP_NAME*.jar $SSH_ADDRESS_1:$DEPLOY_APP_FOLDER/$APP_NAME.jar
  ssh -t -t $SSH_ADDRESS_1 << EOF
  export BASH_ENV=/etc/bash.bashrc && echo "" > $DEPLOY_APP_FOLDER/$APP_NAME.log && $DEPLOY_APP_FOLDER/startStop.sh start && exit
EOF
done

apps=(
#"IxigoProxy"
)


for i in ${!apps[@]}; do
  APP_NAME=${apps[$i]}
  DEPLOY_APP_FOLDER=$BASE_FOLDER/$APP_NAME

  ssh $SSH_ADDRESS_2 mkdir -p $DEPLOY_APP_FOLDER
  scp $WORKSPACE_FOLDER/$APP_NAME/Scripts/startStop.sh $SSH_ADDRESS_2:$DEPLOY_APP_FOLDER
  ssh $SSH_ADDRESS_2 chmod +x $DEPLOY_APP_FOLDER/startStop.sh
  ssh $SSH_ADDRESS_2 $DEPLOY_APP_FOLDER/startStop.sh stop
  sleep 5

  scp $WORKSPACE_FOLDER/$APP_NAME/target/$APP_NAME*.jar $SSH_ADDRESS_2:$DEPLOY_APP_FOLDER/$APP_NAME.jar
  ssh -t -t $SSH_ADDRESS_2 << EOF
  export BASH_ENV=/etc/bash.bashrc && echo "" > $DEPLOY_APP_FOLDER/$APP_NAME.log && $DEPLOY_APP_FOLDER/startStop.sh start && exit
EOF
done


apps=(
"IxigoDiscordBot"
"IxigoEventDispatcher"
"IxigoPlayersManager"
"IxigoRconApi"
"IxigoUi"
"IxigoNotification"
)


for i in ${!apps[@]}; do
  APP_NAME=${apps[$i]}
  DEPLOY_APP_FOLDER=$BASE_FOLDER/$APP_NAME

  ssh $SSH_ADDRESS_3 mkdir -p $DEPLOY_APP_FOLDER
  scp $WORKSPACE_FOLDER/$APP_NAME/Scripts/startStop.sh $SSH_ADDRESS_3:$DEPLOY_APP_FOLDER
  ssh $SSH_ADDRESS_3 chmod +x $DEPLOY_APP_FOLDER/startStop.sh
  ssh $SSH_ADDRESS_3 $DEPLOY_APP_FOLDER/startStop.sh stop
  sleep 5

  scp $WORKSPACE_FOLDER/$APP_NAME/target/$APP_NAME*.jar $SSH_ADDRESS_3:$DEPLOY_APP_FOLDER/$APP_NAME.jar
  ssh -t -t $SSH_ADDRESS_3 << EOF
  export BASH_ENV=/etc/bash.bashrc && echo "" > $DEPLOY_APP_FOLDER/$APP_NAME.log && $DEPLOY_APP_FOLDER/startStop.sh start && exit
EOF
done




