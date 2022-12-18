FROM openjdk:11
EXPOSE 8765:8765
COPY --from=my-maven-img /ixigo/csgo_util/IxigoDiscovery/target/IxigoDiscovery*.jar IxigoDiscovery.jar
COPY ./start.sh start.sh
RUN sed -i -e 's/\r$//' start.sh
RUN chmod +x start.sh
ENTRYPOINT ["/start.sh"]

