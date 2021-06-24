#!/bin/bash

# Clone the repo in the folder specified by the user

git clone -b refactoring https://github.com/marcosolina/csgo_util.git

SCRIPTS_FOLDER=csgo_util/Scripts
YML_FILE=$SCRIPTS_FOLDER/Docker/docker-compose-start-containers.yml
DB_ENV_FILE=$SCRIPTS_FOLDER/Docker/env-database.properties
CONFIG_ENV_FILE=$SCRIPTS_FOLDER/Docker/env-spring-config.properties

#sed -i -e 's/\r$//' $SCRIPTS_FOLDER/*
sed -i -e 's/\r$//' $(find $SCRIPTS_FOLDER -type f -name "*.sh")


echo "Postgres Configuration"
read -p "Type the PostgreSQL user name: " POSTGRES_USER
read -p "Type the PostgreSQL passw: " POSTGRES_PASSW
echo ""
echo "IxigoConfigServer Configuration"
# Set the IxiGo Game server passowrd
read -p "Type the user name for the IxigoConfigServer: " CONFIG_SERVER_USER
read -p "Type the password for the IxigoConfigServer: " CONFIG_SERVER_PASSW
read -p "Type the encryption key for the IxigoConfigServer: " CONFIG_ENC_KEY

sed -i -e "s/__IXIGO_POSTGRES_USER__/$POSTGRES_USER/g" $DB_ENV_FILE
sed -i -e "s/__IXIGO_POSTGRES_PASSW__/$POSTGRES_PASSW/g" $DB_ENV_FILE

sed -i -e "s/__IXIGO_CONFIG_SERVER_USER__/$CONFIG_SERVER_USER/g" $CONFIG_ENV_FILE
sed -i -e "s/__IXIGO_CONFIG_SERVER_PASSW__/$CONFIG_SERVER_PASSW/g" $CONFIG_ENV_FILE
sed -i -e "s/__IXIGO_CONFIG_ENC_KEY__/$CONFIG_ENC_KEY/g" $CONFIG_ENV_FILE

docker-compose -f $YML_FILE up
echo "Run the following command: docker-compose -f $YML_FILE up"