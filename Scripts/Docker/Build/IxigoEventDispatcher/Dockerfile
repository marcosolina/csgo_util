# Putting all together
FROM openjdk:11
EXPOSE 8081:8081
COPY --from=my-maven-img /ixigo/csgo_util/IxigoEventDispatcher/target/IxigoEventDispatcher*.jar IxigoEventDispatcher.jar
COPY ./start.sh start.sh
RUN sed -i -e 's/\r$//' start.sh
RUN chmod +x start.sh

ENTRYPOINT ["/start.sh"]

