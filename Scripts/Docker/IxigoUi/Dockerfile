# Putting all together
FROM openjdk:11
EXPOSE 8086:8086
COPY --from=my-maven-img /ixigo/csgo_util/IxigoUi/target/IxigoUi*.jar IxigoUi.jar
COPY ./start.sh start.sh
RUN sed -i -e 's/\r$//' start.sh
RUN chmod +x start.sh

ENTRYPOINT ["/start.sh"]