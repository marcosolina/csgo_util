# Building the Java "stuff"

FROM maven:3.8.6-openjdk-11 AS MAVEN_IMAGE
RUN mkdir /ixigo
WORKDIR /ixigo

COPY --from=my-nodejs /ixigo/csgo_util ./csgo_util

# Copying the "compiled" React App into the Ixigo UI project
# to make it available in the JAR package
RUN cp -r ./csgo_util/IxigoUi/src/reactapp/build/* ./csgo_util/IxigoUi/src/main/resources/static

RUN \
mvn clean install -f ./csgo_util/IxigoEventDispatcherContract/pom.xml && \
mvn clean install -f ./csgo_util/IxigoLibrary/pom.xml && \
mvn clean install -f ./csgo_util/IxigoParent/pom.xml && \
mvn clean install -f ./csgo_util/IxigoDemManagerContract/pom.xml && \
mvn clean install -f ./csgo_util/IxigoNotificationContract/pom.xml && \
mvn clean install -f ./csgo_util/IxigoDiscordBotContract/pom.xml && \
mvn clean install -f ./csgo_util/IxigoPlayersManagerContract/pom.xml && \
mvn clean install -f ./csgo_util/IxigoRconApiContract/pom.xml && \
mvn clean install -f ./csgo_util/IxigoServerHelperContract/pom.xml && \
mvn clean package -f ./csgo_util/IxigoConfigServer/pom.xml && \
mvn clean package -f ./csgo_util/IxigoDemManager/pom.xml && \
mvn clean package -f ./csgo_util/IxigoDiscordBot/pom.xml && \
mvn clean package -f ./csgo_util/IxigoDiscovery/pom.xml && \
mvn clean package -f ./csgo_util/IxigoEventDispatcher/pom.xml && \
mvn clean package -f ./csgo_util/IxigoPlayersManager/pom.xml && \
mvn clean package -f ./csgo_util/IxigoProxy/pom.xml && \
mvn clean package -f ./csgo_util/IxigoRconApi/pom.xml && \
mvn clean package -f ./csgo_util/IxigoServerHelper/pom.xml && \
mvn clean package -f ./csgo_util/IxigoUi/pom.xml

COPY ./start.sh start.sh
RUN sed -i -e 's/\r$//' start.sh && \
chmod +x start.sh

ENTRYPOINT ["/ixigo/start.sh"]

