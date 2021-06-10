FROM postgres:11

COPY initDemFilesDb.sql  /docker-entrypoint-initdb.d
COPY initDiscordDb.sql  /docker-entrypoint-initdb.d
COPY initEventDispatcherDb.sql  /docker-entrypoint-initdb.d

ENV POSTGRES_PASSWORD=postgres
ENV POSTGRES_USER=postgres