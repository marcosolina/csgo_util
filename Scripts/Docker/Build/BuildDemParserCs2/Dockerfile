FROM node:24-alpine AS NODE_JS
RUN mkdir /ixigo
WORKDIR /ixigo/csgo_util

# Copy the DemoParserCS2 project files
# Build context is now the project root
COPY DemoParserCS2 ./DemoParserCS2

# Install dependencies, ts-node globally, and build TypeScript
WORKDIR /ixigo/csgo_util/DemoParserCS2
RUN npm install && \
    npm install -g ts-node && \
    npx tsc

WORKDIR /ixigo

COPY Scripts/Docker/Build/BuildDemParserCs2/start.sh start.sh
RUN sed -i -e 's/\r$//' start.sh && \
chmod +x start.sh

ENTRYPOINT ["/ixigo/start.sh"]