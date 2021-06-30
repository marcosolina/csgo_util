# Putting all together
FROM openjdk:11
EXPOSE 8082:8082
COPY --from=my_maven /ixigo/csgo_util/IxigoPlayersManager/target/IxigoPlayersManager*.jar IxigoPlayersManager.jar
COPY ./start.sh start.sh
RUN sed -i -e 's/\r$//' start.sh
RUN chmod +x start.sh

ENTRYPOINT ["/start.sh"]

