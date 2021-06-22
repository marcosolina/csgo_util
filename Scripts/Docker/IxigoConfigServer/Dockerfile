FROM maven:3.8.1-jdk-8 AS MAVEN_IMAGE
WORKDIR /tmp
RUN git clone https://github.com/marcosolina/csgo_util.git

RUN mvn clean install -f ./csgo_util/IxigoConfigServer/pom.xml

FROM openjdk:11
EXPOSE 8765:8765
COPY --from=MAVEN_IMAGE /tmp/csgo_util/IxigoConfigServer/target/IxigoConfigServer*.jar IxigoConfigServer.jar
COPY ./start.sh start.sh
RUN sed -i -e 's/\r$//' start.sh
RUN chmod +x start.sh

ENTRYPOINT ["/start.sh"]

