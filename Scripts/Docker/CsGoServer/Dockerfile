###########################################################
# https://github.com/timche/docker-csgo/blob/main/base/Dockerfile
###########################################################

FROM ubuntu:18.04

#RUN mkdir -p /usr/share/man/man1 /usr/share/man/man2
RUN apt-get update \
    && apt-get install -y openjdk-11-jre git wget tar lib32gcc1

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
RUN sed -i -e 's/\r$//' start.sh && chmod +x start.sh

ENTRYPOINT ["/csgoserver/start.sh"]