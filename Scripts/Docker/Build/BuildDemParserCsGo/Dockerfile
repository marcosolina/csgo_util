FROM node:24-alpine AS NODE_JS
RUN mkdir /ixigo
WORKDIR /ixigo/csgo_util

# Copy the DemParserNodeJs project files
# Build context is now the project root
COPY DemParserNodeJs ./DemParserNodeJs

# Install dependencies, ts-node globally, and build TypeScript
WORKDIR /ixigo/csgo_util/DemParserNodeJs
RUN npm install --save demofile && \
    npm install -g ts-node && \
    npx tsc

WORKDIR /ixigo

COPY Scripts/Docker/Build/BuildDemParserCsGo/start.sh start.sh
RUN sed -i -e 's/\r$//' start.sh && \
chmod +x start.sh

ENTRYPOINT ["/ixigo/start.sh"]