FROM openjdk:11
EXPOSE 8765:8765
COPY --from=my_maven /ixigo/csgo_util/IxigoConfigServer/target/IxigoConfigServer*.jar IxigoConfigServer.jar
COPY ./start.sh start.sh
RUN sed -i -e 's/\r$//' start.sh
RUN chmod +x start.sh

ENTRYPOINT ["/start.sh"]

