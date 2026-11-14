# syntax=docker/dockerfile:1
#
# One Dockerfile for every IxiGo image: each image is a --target.
# Build context: the repository root (see the root .dockerignore).
#
#   docker build -f docker/Dockerfile --target dem-manager -t ixigo/dem-manager .
#   docker buildx bake -f docker/compose.build.yml          # every image, multi-arch
#
# The jars and the React bundle do not depend on the CPU, so their stages run on
# the build machine's platform even when the target is arm64 (no emulated Maven
# or webpack). Only the runtime stages and the parsers' npm install (native addon)
# run on the target platform.

ARG JAVA_VERSION=11
ARG NODE_VERSION=24
ARG POSTGRES_VERSION=11

#################################################################################
# Shared build stages
#################################################################################

# React bundle for IxigoUi
FROM --platform=$BUILDPLATFORM node:${NODE_VERSION}-bookworm-slim AS ui-build
ENV NPM_CONFIG_UPDATE_NOTIFIER=false NPM_CONFIG_FUND=false NPM_CONFIG_AUDIT=false
WORKDIR /reactapp
COPY services/IxigoUi/src/reactapp/package.json services/IxigoUi/src/reactapp/package-lock.json ./
RUN --mount=type=cache,target=/root/.npm npm ci
COPY services/IxigoUi/src/reactapp/ ./
RUN npm run build

# Every jar, built once by the root aggregator POM
FROM --platform=$BUILDPLATFORM maven:3.9-eclipse-temurin-${JAVA_VERSION} AS java-build
WORKDIR /src
COPY pom.xml ./
COPY libs/ libs/
COPY services/ services/
# IxigoUi's POM copies this folder into its static resources (the UI cannot start without it)
COPY --from=ui-build /reactapp/build/ services/IxigoUi/src/reactapp/build/
RUN --mount=type=cache,target=/root/.m2 \
    mvn -B -ntp -DskipTests package \
 && mkdir /out \
 && for m in ConfigServer Discovery Proxy DemManager DiscordBot EventDispatcher Notification PlayersManager RconApi ServerHelper Ui; do \
      cp services/Ixigo$m/target/Ixigo$m-*.jar /out/Ixigo$m.jar; \
    done

# Demo parsers run by the DEM manager with "npx ts-node <script> <file>".
# @laihoe/demoparser2 ships a native addon, so this stage runs on the target platform.
FROM node:${NODE_VERSION}-bookworm-slim AS parsers
ENV NPM_CONFIG_UPDATE_NOTIFIER=false NPM_CONFIG_FUND=false NPM_CONFIG_AUDIT=false
WORKDIR /build
COPY parsers/DemoParserCS2/package.json parsers/DemoParserCS2/package-lock.json demParserCs2/
# "npm install", not "npm ci": demoparser2 declares optional platform packages that were
# never published for some releases (darwin-x64, freebsd-x64, ...), and "npm ci" rejects
# a lock file without them. The versions still come from package-lock.json.
RUN --mount=type=cache,target=/root/.npm cd demParserCs2 && npm install
COPY parsers/DemoParserCS2/ demParserCs2/
RUN cd demParserCs2 && npx tsc --noEmit
# CSGO parser: examples/dumpfile.ts needs the dev dependencies too (ansi-styles, typescript, @types).
# No "tsc" build: the script imports the published "demofile" package from node_modules,
# not this folder's dist/, and ts-node compiles it when the DEM manager runs it.
COPY parsers/DemParserNodeJs/package.json parsers/DemParserNodeJs/package-lock.json demParserCsgo/
RUN --mount=type=cache,target=/root/.npm cd demParserCsgo && npm ci
COPY parsers/DemParserNodeJs/ demParserCsgo/

FROM eclipse-temurin:${JAVA_VERSION}-jre AS jre

#################################################################################
# Runtime base for the Java services
#################################################################################

FROM eclipse-temurin:${JAVA_VERSION}-jre AS java-base
# The services write ./logs/<service>.log; keep it writable for any uid set with "user:" in Compose
RUN mkdir -p /app/logs && chmod 1777 /app/logs
WORKDIR /app
ENV JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=75 -XX:+ExitOnOutOfMemoryError"
# uid 1000 is the "ubuntu" user of the base image
USER 1000:1000
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

#################################################################################
# Images (one target each). Ports are the defaults from csgo_props.
#################################################################################

FROM java-base AS config-server
COPY --from=java-build /out/IxigoConfigServer.jar /app/app.jar
EXPOSE 8888

FROM java-base AS discovery
COPY --from=java-build /out/IxigoDiscovery.jar /app/app.jar
EXPOSE 8765

FROM java-base AS proxy
COPY --from=java-build /out/IxigoProxy.jar /app/app.jar
EXPOSE 8763

FROM java-base AS event-dispatcher
COPY --from=java-build /out/IxigoEventDispatcher.jar /app/app.jar
EXPOSE 8081

FROM java-base AS players-manager
COPY --from=java-build /out/IxigoPlayersManager.jar /app/app.jar
EXPOSE 8082

FROM java-base AS rcon-api
COPY --from=java-build /out/IxigoRconApi.jar /app/app.jar
EXPOSE 8083

FROM java-base AS server-helper
COPY --from=java-build /out/IxigoServerHelper.jar /app/app.jar
EXPOSE 8084

FROM java-base AS discord-bot
COPY --from=java-build /out/IxigoDiscordBot.jar /app/app.jar
EXPOSE 8085

FROM java-base AS ui
COPY --from=java-build /out/IxigoUi.jar /app/app.jar
EXPOSE 8086

FROM java-base AS notification
COPY --from=java-build /out/IxigoNotification.jar /app/app.jar
EXPOSE 8087

# DEM manager: Node runtime (for the parsers) plus the Java runtime and the jar.
# Parser paths match the "docker" profile in csgo_props:
#   /demParserCs2/cs2DemoParser.ts and /demParserCsgo/examples/dumpfile.ts
FROM node:${NODE_VERSION}-bookworm-slim AS dem-manager
ENV JAVA_HOME=/opt/java/openjdk \
    PATH=/opt/java/openjdk/bin:$PATH \
    JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=75 -XX:+ExitOnOutOfMemoryError" \
    NPM_CONFIG_UPDATE_NOTIFIER=false \
    NPM_CONFIG_CACHE=/tmp/npm-cache
COPY --from=jre /opt/java/openjdk /opt/java/openjdk
RUN apt-get update \
 && apt-get install -y --no-install-recommends curl \
 && rm -rf /var/lib/apt/lists/* \
 && npm install -g ts-node@10.9.2 \
 && npm cache clean --force \
 && mkdir -p /app/logs /var/www/html/cstrike/demfiles \
 && chmod 1777 /app/logs \
 && chown 1000:1000 /var/www/html/cstrike/demfiles
COPY --from=parsers /build/demParserCs2 /demParserCs2
COPY --from=parsers /build/demParserCsgo /demParserCsgo
COPY --from=java-build /out/IxigoDemManager.jar /app/app.jar
WORKDIR /app
# uid 1000 is the "node" user of the base image; the dem files folder is a host mount
USER 1000:1000
EXPOSE 8080 8090
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

# PostgreSQL with the schemas and seed data. The init scripts run only on an empty
# data directory, which is every deploy (the database is rebuilt from the dem files).
FROM postgres:${POSTGRES_VERSION} AS postgres
COPY docker/postgres/init/ /docker-entrypoint-initdb.d/
