FROM postgres:11

# https://www.digitalocean.com/community/tutorials/how-to-remove-docker-images-containers-and-volumes

COPY 010-initBaseDemFilesDb.sql                         /docker-entrypoint-initdb.d
COPY 011-initFunctionsDemFilesDb.sql                    /docker-entrypoint-initdb.d
COPY 012-initViewsDemFilesDb.sql                        /docker-entrypoint-initdb.d
COPY 015-initIndexesDemFilesDb.sql                      /docker-entrypoint-initdb.d
COPY 016-seedBaseData.sql                               /docker-entrypoint-initdb.d
#COPY 018-optimizedNewDemFilesDb_views.sql               /docker-entrypoint-initdb.d
COPY 020-initDiscordDb.sql                              /docker-entrypoint-initdb.d
COPY 030-initEventDispatcherDb.sql                      /docker-entrypoint-initdb.d

#ENV POSTGRES_USER=REPLACE_ME
#ENV POSTGRES_PASSWORD=REPLACE_ME