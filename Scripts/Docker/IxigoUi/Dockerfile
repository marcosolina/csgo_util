# Putting all together
FROM openjdk:11
EXPOSE 8089:8089
COPY --from=my_maven /ixigo/csgo_util/IxigoUi/target/IxigoUi*.jar IxigoUi.jar
COPY ./start.sh start.sh
RUN sed -i -e 's/\r$//' start.sh
RUN chmod +x start.sh

ENTRYPOINT ["/start.sh"]

