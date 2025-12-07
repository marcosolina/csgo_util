# Putting all together
FROM maven:3.8.6-openjdk-11
EXPOSE 8080:8080

# Disable npm update notifications
ENV NPM_CONFIG_UPDATE_NOTIFIER=false

# Install Node.js 24.x LTS runtime in the final image
RUN apt-get update && \
    curl -fsSL https://deb.nodesource.com/setup_24.x | bash - && \
    apt-get install -y nodejs && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# Create directories and copy Node.js parser from the build stage
RUN mkdir /demFiles && mkdir /demParserCs2 && mkdir /demParserCsgo
COPY --from=my-demcs2parser-img /ixigo/csgo_util/DemoParserCS2/node_modules /demParserCs2/node_modules
COPY --from=my-demcs2parser-img /ixigo/csgo_util/DemoParserCS2/*.js /demParserCs2/
COPY --from=my-demcs2parser-img /ixigo/csgo_util/DemoParserCS2/package*.json /demParserCs2/

COPY --from=my-demcsgoparser-img /ixigo/csgo_util/DemParserNodeJs /demParserCsgo

# Install ts-node globally in the final image
RUN npm install -g ts-node

# Copy Spring Boot application
COPY --from=my-maven-img /ixigo/csgo_util/IxigoDemManager/target/IxigoDemManager*.jar IxigoDemManager.jar

# Copy and prepare start script
COPY ./start.sh start.sh
RUN sed -i -e 's/\r$//' start.sh
RUN chmod +x start.sh

ENTRYPOINT ["/start.sh"]

