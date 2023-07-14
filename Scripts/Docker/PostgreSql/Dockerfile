FROM postgres:11

#COPY initDemFilesDb.sql  /docker-entrypoint-initdb.d
COPY initNewDemFilesDb.sql  /docker-entrypoint-initdb.d
COPY initDiscordDb.sql  /docker-entrypoint-initdb.d
COPY initEventDispatcherDb.sql  /docker-entrypoint-initdb.d

#ENV POSTGRES_USER=REPLACE_ME
#ENV POSTGRES_PASSWORD=REPLACE_ME