#!/bin/bash

# Clone the repo in the folder specified by the user

git clone https://github.com/marcosolina/csgo_util.git


SCRIPTS_FOLDER=csgo_util/Scripts
YML_FILE=$SCRIPTS_FOLDER/Docker/docker-compose-start-containers.yml

echo "IxigoConfigServer Configuration"
# Set the IxiGo Game server passowrd
read -p "Choose the user name for the IxigoConfigServer: " IXIGO_CONFIG_SERVER_USER
read -p "Choose the password for the IxigoConfigServer: " IXIGO_CONFIG_SERVER_PASSW
read -p "Choose the encryption key for the IxigoConfigServer: " IXIGO_CONFIG_ENC_KEY

sed -i -e "s/__IXIGO_CONFIG_SERVER_USER__/$IXIGO_CONFIG_SERVER_USER/g" $YML_FILE
sed -i -e "s/__IXIGO_CONFIG_SERVER_PASSW__/$IXIGO_CONFIG_SERVER_PASSW/g" $YML_FILE
sed -i -e "s/__IXIGO_CONFIG_ENC_KEY__/$IXIGO_CONFIG_ENC_KEY/g" $YML_FILE

echo "Run the following command: docker-compose -f $YML_FILE up"