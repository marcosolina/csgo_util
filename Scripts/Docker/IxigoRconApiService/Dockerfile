# Putting all together
FROM openjdk:11
EXPOSE 8084:8084
COPY --from=my_maven /ixigo/csgo_util/IxigoRconApiService/target/IxigoRconApiService*.jar IxigoRconApiService.jar
COPY ./start.sh start.sh
RUN sed -i -e 's/\r$//' start.sh
RUN chmod +x start.sh

ENTRYPOINT ["/start.sh"]

