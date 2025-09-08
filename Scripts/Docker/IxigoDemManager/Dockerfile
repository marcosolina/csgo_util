# Use a base image with Ubuntu
FROM ubuntu:20.04

# Install dependencies
RUN apt-get update && apt-get install -y \
    curl \
    openjdk-11-jdk \
    nodejs \
    npm

ENV JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64
ENV PATH=$JAVA_HOME/bin:$PATH

RUN mkdir /ixigo
WORKDIR /ixigo

# Copy the CS2 dem parser
COPY --from=my-nodejs-img /ixigo/csgo_util/DemoParserCS2 ./


EXPOSE 8080:8080
RUN mkdir /demFiles
COPY --from=my-maven-img /ixigo/csgo_util/IxigoDemManager/target/IxigoDemManager*.jar IxigoDemManager.jar
COPY ./start.sh start.sh
RUN sed -i -e 's/\r$//' start.sh
RUN chmod +x start.sh && chmod +x /demParser/DemParser


ENTRYPOINT ["/start.sh"]

