# Putting all together
FROM openjdk:11
EXPOSE 8082:8082
COPY --from=my_maven /ixigo/csgo_util/IxigoDiscordBot/target/IxigoDiscordBot*.jar IxigoDiscordBot.jar
COPY ./start.sh start.sh
RUN sed -i -e 's/\r$//' start.sh
RUN chmod +x start.sh

ENTRYPOINT ["/start.sh"]

