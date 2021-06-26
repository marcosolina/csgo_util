# Building the Java "stuff"

FROM maven:3.8.1-jdk-8 AS MAVEN_IMAGE
RUN mkdir /ixigo
WORKDIR /ixigo
RUN git clone -b refactoring https://github.com/marcosolina/csgo_util.git
RUN git clone https://github.com/marcosolina/javautils.git
RUN git clone https://github.com/marcosolina/WebJar.git

RUN mvn clean install -f javautils/Utils/pom.xml
RUN mvn clean install -f WebJar/pom.xml
RUN mvn clean install -f ./csgo_util/IxigoDiscovery/pom.xml
RUN mvn clean install -f ./csgo_util/IxigoConfigServer/pom.xml
RUN mvn clean install -f ./csgo_util/IxigoDemManager/pom.xml
RUN mvn clean install -f ./csgo_util/IxigoDiscordBot/pom.xml
RUN mvn clean install -f ./csgo_util/IxigoEventDispatcher/pom.xml
RUN mvn clean install -f ./csgo_util/IxigoPlayersManager/pom.xml
RUN mvn clean install -f ./csgo_util/IxigoProxy/pom.xml
RUN mvn clean install -f ./csgo_util/IxigoRconApiService/pom.xml
RUN mvn clean install -f ./csgo_util/IxigoServerHelper/pom.xml
RUN mvn clean install -f ./csgo_util/IxigoUi/pom.xml


COPY ./start.sh start.sh
RUN sed -i -e 's/\r$//' start.sh
RUN chmod +x start.sh

ENTRYPOINT ["/start.sh"]

