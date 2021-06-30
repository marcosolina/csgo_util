#!/bin/bash

sleep 90

BASE_DIR=/csgoserver
REPO_DIRECTORY=$BASE_DIR/ixi_go
CSGO_SERVER_DIR=$REPO_DIRECTORY/CsgoServer
JAR_SERVICE=$BASE_DIR/IxigoServerHelper.jar

chown steam:steam -R $BASE_DIR
chmod +x $JAR_SERVICE
ls -l $BASE_DIR

if [ ! -d "$CSGO_SERVER_DIR" ]; then
    su - steam -c "mkdir -p $REPO_DIRECTORY"
    su - steam -c "git clone https://github.com/marcosolina/ixi_go.git $REPO_DIRECTORY"
fi

su - steam -c "nohup java -jar $JAR_SERVICE &"

rm -rf $CSGO_SERVER_DIR/csgo/*.dem
rm -rf $CSGO_SERVER_DIR/csgo/backup_round*.txt

HOST_IP=$(hostname -I | awk '{print $1}')

su - steam -c "/home/steam/steamcmd/steamcmd.sh +login anonymous +force_install_dir $CSGO_SERVER_DIR +app_update 740 +quit"
su - steam -c "$CSGO_SERVER_DIR/srcds_run -game csgo -console -usercon -port 27015 +ip $HOST_IP +game_type 0 +game_mode 1 +mapgroup mg_ixico_maps +map ar_dizzy -authkey $ENV_STEAM_API_KEY +sv_setsteamaccount $ENV_STEAM_CSGO_KEY -net_port_try 1"