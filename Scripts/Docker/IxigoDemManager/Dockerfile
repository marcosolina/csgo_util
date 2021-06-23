FROM maven:3.8.1-jdk-8 AS MAVEN_IMAGE
WORKDIR /tmp
RUN git clone https://github.com/marcosolina/javautils.git
RUN git clone https://github.com/marcosolina/csgo_util.git

RUN mvn clean install -f javautils/Utils
RUN mvn clean install -f ./csgo_util/IxigoDemManager/pom.xml

FROM openjdk:11
EXPOSE 8081:8081
COPY --from=MAVEN_IMAGE /tmp/csgo_util/IxigoDemManager/target/IxigoDemManager*.jar IxigoDemManager.jar
COPY ./start.sh start.sh
RUN sed -i -e 's/\r$//' start.sh
RUN chmod +x start.sh

ENTRYPOINT ["/start.sh"]

