#!/bin/bash

sleep 90

REPO_DIRECTORY=$TOP_FOLDER/ixi_go
CSGO_SERVER_DIR=$SERVER_FOLDER/CsgoServer
JAR_SERVICE=$TOP_FOLDER/IxigoServerHelper.jar
export ENV_CSGO_INSTALL_FOLDER=$CSGO_SERVER_DIR # property used by the java service

if [ ! -d "$CSGO_SERVER_DIR" ]; then
    mkdir -p $SERVER_FOLDER
    git clone https://github.com/marcosolina/ixi_go.git $SERVER_FOLDER

    tail -100 $CSGO_SERVER_DIR/csgo/csg/server.cfg
    sed -i -e "s/RCON_PASSWORD/$IXIGO_CSGO_PASSW/g" $CSGO_SERVER_DIR/csgo/cfg/server.cfg
    sed -i -e "s/SERVER_PASSWORD/$IXIGO_CSGO_PASSW/g" $CSGO_SERVER_DIR/csgo/cfg/server.cfg
    tail -100 $CSGO_SERVER_DIR/csgo/csg/server.cfg
    sleep 30
fi

EVENT_FILE=$CSGO_SERVER_DIR/csgo/addons/sourcemod/event.txt
echo "NO" > $EVENT_FILE

nohup java -jar $JAR_SERVICE \
--spring.cloud.config.uri=$IXIGO_CONFIG_SERVER_URI \
--spring.cloud.config.username=$IXIGO_CONFIG_SERVER_USER \
--spring.cloud.config.password=$IXIGO_CONFIG_SERVER_PASSW \
--eureka.client.serviceUrl.defaultZone=$IXIGO_EUREKA_SERVER \
--spring.profiles.active=$IXIGO_PROFILE &

rm -rf $CSGO_SERVER_DIR/csgo/*.dem
rm -rf $CSGO_SERVER_DIR/csgo/backup_round*.txt

HOST_IP=$(hostname -I | awk '{print $1}')

$STEAM_FOLDER/steamcmd.sh +login anonymous +force_install_dir $CSGO_SERVER_DIR +app_update 740 +quit
$CSGO_SERVER_DIR/srcds_run -game csgo -console -usercon -port 27015 +ip $HOST_IP +game_type 0 +game_mode 1 +mapgroup mg_ixico_maps +map ar_dizzy -authkey $ENV_STEAM_API_KEY +sv_setsteamaccount $ENV_STEAM_CSGO_KEY -net_port_try 1