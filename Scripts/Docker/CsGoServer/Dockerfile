###########################################################
# https://github.com/timche/docker-csgo/blob/main/base/Dockerfile
###########################################################

FROM ubuntu:18.04

ENV DEBIAN_FRONTEND noninteractive
RUN apt-get update && apt-get install -y ubuntu-server

ENV TOP_FOLDER /csgoserver
ENV STEAM_FOLDER "${TOP_FOLDER}/steam"
ENV SERVER_FOLDER "${TOP_FOLDER}/ixi_go"

RUN apt-get update && apt-get install software-properties-common -y && add-apt-repository multiverse
RUN dpkg --add-architecture i386
RUN apt-get update \
    && apt-get install -y \
	lib32gcc1 \
	openjdk-11-jre \
	git \
	wget \
	tar \
	ca-certificates \
    net-tools \
    lib32stdc++6 \
    lib32z1 \
    lib32z1-dev \
	curl

RUN mkdir -p "${SERVER_FOLDER}" \
&& mkdir -p "${STEAM_FOLDER}" \
&& useradd -m csgo \
&& chown csgo:csgo -R "${TOP_FOLDER}"

USER csgo

WORKDIR ${STEAM_FOLDER}

RUN wget -qO- https://steamcdn-a.akamaihd.net/client/installer/steamcmd_linux.tar.gz | tar zxf -
RUN chmod +x -R ./*

WORKDIR ${TOP_FOLDER}

COPY --from=my_maven /ixigo/csgo_util/IxigoServerHelper/target/IxigoServerHelper*.jar IxigoServerHelper.jar

# Expose ports
EXPOSE 27015/tcp \
	27015/udp \
	27020/udp \
	27005/udp \
	51840/udp

COPY ./start.sh start.sh
RUN sed -i -e 's/\r$//' start.sh && chmod +x start.sh

ENTRYPOINT ["/csgoserver/start.sh"]