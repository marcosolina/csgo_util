###########################################################
# Dockerfile that builds a CSGO Gameserver
###########################################################
FROM cm2network/steamcmd:root

# Installing Java https://stackoverflow.com/questions/61815233/install-java-runtime-in-debian-based-docker-image
ENV DEBIAN_FRONTEND=noninteractive
RUN mkdir -p /usr/share/man/man1 /usr/share/man/man2
RUN apt-get update && \
apt-get install -y --no-install-recommends \
        openjdk-11-jre

# Installing preparing the folders for our CSGO
RUN mkdir -p /csgoserver/ixi_go && chown steam:steam -R /csgoserver
WORKDIR /csgoserver
RUN apt update && apt-get install git-core -y
COPY --from=my_maven /ixigo/csgo_util/IxigoServerHelper/target/IxigoServerHelper*.jar IxigoServerHelper.jar

# Expose ports
EXPOSE 27015/tcp \
	27015/udp \
	27020/udp

COPY ./start.sh start.sh
RUN sed -i -e 's/\r$//' start.sh
RUN chmod +x start.sh && chown steam:steam -R /csgoserver
RUN ls -l /
RUN ls -l

ENTRYPOINT ["/csgoserver/start.sh"]