#!/bin/bash

WORKSPACE_FOLDER=$1



# Build the Java projects
mvn clean install -f $WORKSPACE_FOLDER/IxigoLibrary/pom.xml
mvn clean install -f $WORKSPACE_FOLDER/IxigoParent/pom.xml
mvn clean install -f $WORKSPACE_FOLDER/IxigoDemManagerContract/pom.xml
mvn clean install -f $WORKSPACE_FOLDER/IxigoDiscordBotContract/pom.xml
mvn clean install -f $WORKSPACE_FOLDER/IxigoEventDispatcherContract/pom.xml
mvn clean install -f $WORKSPACE_FOLDER/IxigoPlayersManagerContract/pom.xml
mvn clean install -f $WORKSPACE_FOLDER/IxigoRconApiContract/pom.xml
mvn clean install -f $WORKSPACE_FOLDER/IxigoServerHelperContract/pom.xml
mvn clean install -f $WORKSPACE_FOLDER/IxigoNotificationContract/pom.xml
mvn clean package -f $WORKSPACE_FOLDER/IxigoDemManager/pom.xml
