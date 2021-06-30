###########################################################
# https://github.com/timche/docker-csgo/blob/main/base/Dockerfile
###########################################################

FROM debian:buster-slim

RUN mkdir -p /usr/share/man/man1 /usr/share/man/man2
RUN apt-get update \
    && apt-get install -y --no-install-recommends \
    ca-certificates=20200601~deb10u2 \
    lib32gcc1=1:8.3.0-6 \
    lib32stdc++6=8.3.0-6 \
    rsync=3.1.3-6 \
    unzip=6.0-23+deb10u2 \
    wget=1.20.1-1.1 \
    && rm -rf /var/lib/apt/lists/* \
    && useradd -m csgo \
	&& openjdk-11-jre \
	&& git-core


RUN mkdir -p /csgoserver/ixi_go && mkdir -p /csgoserver/steam
WORKDIR /csgoserver/steam

RUN wget -qO- https://steamcdn-a.akamaihd.net/client/installer/steamcmd_linux.tar.gz | tar zxf -

WORKDIR /csgoserver

COPY --from=my_maven /ixigo/csgo_util/IxigoServerHelper/target/IxigoServerHelper*.jar IxigoServerHelper.jar

# Expose ports
EXPOSE 27015/tcp \
	27015/udp \
	27020/udp \
	27005/udp \
	51840/udp

COPY ./start.sh start.sh
RUN sed -i -e 's/\r$//' start.sh && chmod +x start.sh && chown steam:steam -R /csgoserver

ENTRYPOINT ["/csgoserver/start.sh"]