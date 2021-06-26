# Putting all together
FROM openjdk:11
EXPOSE 8086:8086
COPY --from=my_maven /ixigo/csgo_util/IxigoEventDispathcer/target/IxigoEventDispathcer*.jar IxigoEventDispathcer.jar
COPY ./start.sh start.sh
RUN sed -i -e 's/\r$//' start.sh
RUN chmod +x start.sh

ENTRYPOINT ["/start.sh"]

