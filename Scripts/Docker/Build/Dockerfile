# Building the Java "stuff"

FROM maven:3.8.1-jdk-8 AS MAVEN_IMAGE
RUN mkdir /ixigo
WORKDIR /ixigo
RUN git clone https://github.com/marcosolina/csgo_util.git && \
git clone https://github.com/marcosolina/javautils.git && \
git clone https://github.com/marcosolina/WebJar.git

RUN mvn clean install -f javautils/Utils/pom.xml && \
mvn clean install -f javautils/Partitioning/pom.xml && \
mvn clean install -f WebJar/pom.xml && \
mvn clean package -f ./csgo_util/IxigoDiscovery/pom.xml && \
mvn clean package -f ./csgo_util/IxigoConfigServer/pom.xml && \
mvn clean package -f ./csgo_util/IxigoDemManager/pom.xml && \
mvn clean package -f ./csgo_util/IxigoDiscordBot/pom.xml && \
mvn clean package -f ./csgo_util/IxigoEventDispatcher/pom.xml && \
mvn clean package -f ./csgo_util/IxigoPlayersManager/pom.xml && \
mvn clean package -f ./csgo_util/IxigoProxy/pom.xml && \
mvn clean package -f ./csgo_util/IxigoRconApiService/pom.xml && \
mvn clean package -f ./csgo_util/IxigoServerHelper/pom.xml && \
mvn clean package -f ./csgo_util/IxigoUi/pom.xml


COPY ./start.sh start.sh
RUN sed -i -e 's/\r$//' start.sh && \
chmod +x start.sh

ENTRYPOINT ["/ixigo/start.sh"]

