# Putting all together
FROM openjdk:11
EXPOSE 8763:8763
COPY --from=my-maven-img /ixigo/csgo_util/IxigoProxy/target/IxigoProxy*.jar IxigoProxy.jar
COPY ./start.sh start.sh
RUN sed -i -e 's/\r$//' start.sh
RUN chmod +x start.sh

ENTRYPOINT ["/start.sh"]

