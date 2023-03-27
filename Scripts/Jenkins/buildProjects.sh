#!/bin/bash

WORKSPACE_FOLDER=$1

# Build the React JS app
cd $WORKSPACE_FOLDER/IxigoUi/src/reactapp
npm install
npm run build

cp ./build/* $WORKSPACE_FOLDER/IxigoUi/src/main/resources/static

mvn clean install -f $WORKSPACE_FOLDER/IxigoLibrary/pom.xml
mvn clean install -f $WORKSPACE_FOLDER/IxigoParent/pom.xml
mvn clean install -f $WORKSPACE_FOLDER/IxigoDemManagerContract/pom.xml
mvn clean install -f $WORKSPACE_FOLDER/IxigoDiscordBotContract/pom.xml
mvn clean install -f $WORKSPACE_FOLDER/IxigoEventDispatcherContract/pom.xml
mvn clean install -f $WORKSPACE_FOLDER/IxigoPlayersManagerContract/pom.xml
mvn clean install -f $WORKSPACE_FOLDER/IxigoRconApiContract/pom.xml
mvn clean install -f $WORKSPACE_FOLDER/IxigoServerHelperContract/pom.xml
mvn clean package -f $WORKSPACE_FOLDER/IxigoConfigServer/pom.xml
mvn clean package -f $WORKSPACE_FOLDER/IxigoDemManager/pom.xml
mvn clean package -f $WORKSPACE_FOLDER/IxigoDiscordBot/pom.xml
mvn clean package -f $WORKSPACE_FOLDER/IxigoDiscovery/pom.xml
mvn clean package -f $WORKSPACE_FOLDER/IxigoEventDispatcher/pom.xml
mvn clean package -f $WORKSPACE_FOLDER/IxigoPlayersManager/pom.xml
mvn clean package -f $WORKSPACE_FOLDER/IxigoProxy/pom.xml
mvn clean package -f $WORKSPACE_FOLDER/IxigoRconApi/pom.xml
mvn clean package -f $WORKSPACE_FOLDER/IxigoServerHelper/pom.xml
mvn clean package -f $WORKSPACE_FOLDER/IxigoUi/pom.xml

