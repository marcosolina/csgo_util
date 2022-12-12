# Building the Java "stuff"

FROM maven:3.8.6-openjdk-18 AS MAVEN_IMAGE
RUN mkdir /ixigo
WORKDIR /ixigo
RUN git clone --branch refactoring https://github.com/marcosolina/csgo_util.git

RUN \
mvn clean install -f ./csgo_util/IxigoLibrary/pom.xml && \
mvn clean install -f ./csgo_util/IxigoParent/pom.xml && \
mvn clean install -f ./csgo_util/IxigoDemManagerContract/pom.xml && \
mvn clean install -f ./csgo_util/IxigoDiscordBotContract/pom.xml && \
mvn clean install -f ./csgo_util/IxigoEventDispatcherContract/pom.xml && \
mvn clean install -f ./csgo_util/IxigoPlayersManagerContract/pom.xml && \
mvn clean install -f ./csgo_util/IxigoRconApiContract/pom.xml && \
mvn clean package -f ./csgo_util/IxigoConfigServer/pom.xml && \
mvn clean package -f ./csgo_util/IxigoDemManager/pom.xml && \
mvn clean package -f ./csgo_util/IxigoDiscordBot/pom.xml && \
mvn clean package -f ./csgo_util/IxigoDiscovery/pom.xml && \
mvn clean package -f ./csgo_util/IxigoEventDispatcher/pom.xml && \
mvn clean package -f ./csgo_util/IxigoPlayersManager/pom.xml && \
mvn clean package -f ./csgo_util/IxigoProxy/pom.xml && \
mvn clean package -f ./csgo_util/IxigoRconApi/pom.xml && \
mvn clean package -f ./csgo_util/IxigoServerHelper/pom.xml


COPY ./start.sh start.sh
RUN sed -i -e 's/\r$//' start.sh && \
chmod +x start.sh

ENTRYPOINT ["/ixigo/start.sh"]

