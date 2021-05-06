FROM postgres:11

COPY initDiscordDb.sql  /docker-entrypoint-initdb.d

ENV POSTGRES_PASSWORD=postgres
ENV POSTGRES_USER=postgres