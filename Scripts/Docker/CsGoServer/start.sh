#!/bin/bash

sleep 90

REPO_DIRECTORY=$TOP_FOLDER/ixi_go
CSGO_SERVER_DIR=$SERVER_FOLDER/CsgoServer
CSGO_DIR=$CSGO_SERVER_DIR/csgo
JAR_SERVICE=$TOP_FOLDER/IxigoServerHelper.jar
export ENV_CSGO_INSTALL_FOLDER=$CSGO_SERVER_DIR # property used by the java service

if [ ! -d "$CSGO_SERVER_DIR" ]; then
    mkdir -p $SERVER_FOLDER
    git clone https://github.com/marcosolina/ixi_go.git $SERVER_FOLDER

    sed -i -e "s/RCON_PASSWORD/$IXIGO_CSGO_PASSW/g" $CSGO_SERVER_DIR/csgo/cfg/server.cfg
    sed -i -e "s/SERVER_PASSWORD/$IXIGO_CSGO_PASSW/g" $CSGO_SERVER_DIR/csgo/cfg/server.cfg

    # Are we in a metamod container and is the metamod folder missing?
    if  [ ! -z "$ENV_METAMOD_VERSION" ] && [ ! -d "${CSGO_DIR}/addons/metamod" ]; then
        LATESTMM=$(wget -qO- https://mms.alliedmods.net/mmsdrop/"${ENV_METAMOD_VERSION}"/mmsource-latest-linux)
        wget -qO- https://mms.alliedmods.net/mmsdrop/"${ENV_METAMOD_VERSION}"/"${LATESTMM}" | tar xvzf - -C "${CSGO_DIR}"	
    fi

    # Are we in a sourcemod container and is the sourcemod folder missing?
    if  [ ! -z "$ENV_SOURCEMOD_VERSION" ] && [ ! -d "${CSGO_DIR}/addons/sourcemod" ]; then
        LATESTSM=$(wget -qO- https://sm.alliedmods.net/smdrop/"${ENV_SOURCEMOD_VERSION}"/sourcemod-latest-linux)
        wget -qO- https://sm.alliedmods.net/smdrop/"${ENV_SOURCEMOD_VERSION}"/"${LATESTSM}" | tar xvzf - -C "${CSGO_DIR}"
        cp -r $REPO_DIRECTORY/Scripts/csgo/addons/sourcemod/plugins/* ${CSGO_DIR}/addons/sourcemod/plugins
    fi
fi

EVENT_FILE=$CSGO_SERVER_DIR/csgo/addons/sourcemod/event.txt
echo "NO" > $EVENT_FILE

nohup java -jar $JAR_SERVICE &

#rm -rf $CSGO_SERVER_DIR/csgo/*.dem
rm -rf $CSGO_SERVER_DIR/csgo/backup_round*.txt

HOST_IP=$(hostname -I | awk '{print $1}')

# Some small maps to be used as "starting map".
# Use a small map while we wait for everybody to join
maps=(
"cs_militia"
"de_stmarc"
"de_lake"
"de_safehouse"
)

arrSize=$((${#maps[@]} - 1))
randomMapIndex=$(($RANDOM % $arrSize))
startMap=$randomMapIndex
MAP_START=${maps[$startMap]}

cd "${CSGO_SERVER_DIR}"

$STEAM_FOLDER/steamcmd.sh +force_install_dir "$CSGO_SERVER_DIR" +login anonymous +app_update 740 +quit
$CSGO_SERVER_DIR/srcds_run -game csgo \
    -console \
    -usercon \
    -port 27015 \
    +ip $HOST_IP \
    +game_type 0 \
    +game_mode 1 \
    +mapgroup mg_all_maps \
    +map $MAP_START \
    -authkey $ENV_STEAM_API_KEY \
    +sv_setsteamaccount $ENV_STEAM_CSGO_KEY \
    -net_port_try 1