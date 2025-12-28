FROM openjdk:11
EXPOSE 8888:8888
COPY --from=my-maven-img /ixigo/csgo_util/IxigoConfigServer/target/IxigoConfigServer*.jar IxigoConfigServer.jar
COPY ./start.sh start.sh
RUN sed -i -e 's/\r$//' start.sh
RUN chmod +x start.sh

ENTRYPOINT ["/start.sh"]

