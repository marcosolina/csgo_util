# Putting all together
FROM openjdk:11
EXPOSE 8083:8083
COPY --from=my-maven-img /ixigo/csgo_util/IxigoRconApi/target/IxigoRconApi*.jar IxigoRconApi.jar
COPY ./start.sh start.sh
RUN sed -i -e 's/\r$//' start.sh
RUN chmod +x start.sh

ENTRYPOINT ["/start.sh"]

