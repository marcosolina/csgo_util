#!/bin/bash

sleep 90

REPO_DIRECTORY=$TOP_FOLDER/ixi_go
CSGO_SERVER_DIR=$SERVER_FOLDER/CsgoServer
JAR_SERVICE=$TOP_FOLDER/IxigoServerHelper.jar
export ENV_CSGO_INSTALL_FOLDER=$CSGO_SERVER_DIR # property used by the java service

if [ ! -d "$CSGO_SERVER_DIR" ]; then
    mkdir -p $SERVER_FOLDER
    git clone https://github.com/marcosolina/ixi_go.git $SERVER_FOLDER

    sed -i -e "s/RCON_PASSWORD/$IXIGO_CSGO_PASSW/g" $CSGO_SERVER_DIR/csgo/cfg/server.cfg
    sed -i -e "s/SERVER_PASSWORD/$IXIGO_CSGO_PASSW/g" $CSGO_SERVER_DIR/csgo/cfg/server.cfg
fi

EVENT_FILE=$CSGO_SERVER_DIR/csgo/addons/sourcemod/event.txt
echo "NO" > $EVENT_FILE

nohup java -jar $JAR_SERVICE \
--spring.cloud.config.uri=$IXIGO_CONFIG_SERVER_URI \
--spring.cloud.config.username=$IXIGO_CONFIG_SERVER_USER \
--spring.cloud.config.password=$IXIGO_CONFIG_SERVER_PASSW \
--eureka.client.serviceUrl.defaultZone=$IXIGO_EUREKA_SERVER \
--spring.profiles.active=$IXIGO_PROFILE &

#rm -rf $CSGO_SERVER_DIR/csgo/*.dem
rm -rf $CSGO_SERVER_DIR/csgo/backup_round*.txt

HOST_IP=$(hostname -I | awk '{print $1}')

# Some small maps to be used as "starting map".
# Use a small map while we wait for everybody to join
maps=(
"ar_dizzy"
"de_pitstop"
"de_calavera"
"cs_militia"
"de_bank"
"de_guard"
"de_lake"
"de_safehouse"
"de_stmarc"
"workshop/135827566/cs_estate"
"workshop/127012360/cs_museum"
"workshop/1561348377/de_aztec"
"workshop/1302060184/de_beerhouse"
"workshop/1387732091/de_dst"
"workshop/862889198/de_westwood2"
"workshop/523638720/fy_simpsons"
)

$arrSize=${#arr[@]}-1
$randomMapIndex=$(($RANDOM % $arrSize))
startMap=$randomMapIndex
MAP_START=${maps[$startMap]}

$STEAM_FOLDER/steamcmd.sh +login anonymous +force_install_dir $CSGO_SERVER_DIR +app_update 740 +quit
$CSGO_SERVER_DIR/srcds_run -game csgo -console -usercon -port 27015 +ip $HOST_IP +game_type 0 +game_mode 1 +mapgroup mg_ixico_maps +map $MAP_START -authkey $ENV_STEAM_API_KEY +sv_setsteamaccount $ENV_STEAM_CSGO_KEY -net_port_try 1