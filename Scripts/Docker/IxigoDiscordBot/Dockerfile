# Putting all together
FROM openjdk:11
EXPOSE 8085:8085
COPY --from=my-maven-img /ixigo/csgo_util/IxigoDiscordBot/target/IxigoDiscordBot*.jar IxigoDiscordBot.jar
COPY ./start.sh start.sh
RUN sed -i -e 's/\r$//' start.sh
RUN chmod +x start.sh

ENTRYPOINT ["/start.sh"]