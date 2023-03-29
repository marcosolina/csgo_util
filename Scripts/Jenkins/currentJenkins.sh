BUILD_ID=dontKillMe

chmod +x $WORKSPACE/Scripts/Jenkins/*
sed -i -e 's/\r$//' $WORKSPACE/Scripts/Jenkins/deployDemParser.sh
sed -i -e 's/\r$//' $WORKSPACE/Scripts/Jenkins/deployJar.sh

RASP_1=192.168.1.26
RASP_2=192.168.1.27
RASP_3=192.168.1.30
BASE_FOLDER=/opt/ixigo

ssh pi@$RASP_1 mkdir -p $BASE_FOLDER
ssh pi@$RASP_2 mkdir -p $BASE_FOLDER
ssh pi@$RASP_3 mkdir -p $BASE_FOLDER

#########################################################
# C# Project
#########################################################
$WORKSPACE/Scripts/Jenkins/deployDemParser.sh "DemParser" "$RASP_2" "$WORKSPACE" "$BASE_FOLDER"

#########################################################
# Deploying the JARS
#########################################################

$WORKSPACE/Scripts/Jenkins/deployJar.sh "IxigoDiscovery" "$RASP_2" "$WORKSPACE" "$BASE_FOLDER"
sleep 90
#$WORKSPACE/Scripts/Jenkins/deployJar.sh "IxigoConfigServer" "$RASP_1" "$WORKSPACE" "$BASE_FOLDER"
#sleep 90
$WORKSPACE/Scripts/Jenkins/deployJar.sh "IxigoDemManager" "$RASP_2" "$WORKSPACE" "$BASE_FOLDER"
$WORKSPACE/Scripts/Jenkins/deployJar.sh "IxigoEventDispatcher" "$RASP_2" "$WORKSPACE" "$BASE_FOLDER"
$WORKSPACE/Scripts/Jenkins/deployJar.sh "IxigoPlayersManager" "$RASP_2" "$WORKSPACE" "$BASE_FOLDER"
$WORKSPACE/Scripts/Jenkins/deployJar.sh "IxigoRconApiService" "$RASP_3" "$WORKSPACE" "$BASE_FOLDER"
$WORKSPACE/Scripts/Jenkins/deployJar.sh "IxigoDiscordBot" "$RASP_3" "$WORKSPACE" "$BASE_FOLDER"
$WORKSPACE/Scripts/Jenkins/deployJar.sh "IxigoProxy" "$RASP_3" "$WORKSPACE" "$BASE_FOLDER"
$WORKSPACE/Scripts/Jenkins/deployJar.sh "IxigoUi" "$RASP_2" "$WORKSPACE" "$BASE_FOLDER"

