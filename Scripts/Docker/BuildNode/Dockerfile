FROM node:18.13 AS NODE_JS
RUN mkdir /ixigo
WORKDIR /ixigo

RUN git clone --branch main https://github.com/marcosolina/csgo_util.git

RUN cd csgo_util/IxigoUi/src/reactapp && npm install && npm run build
RUN cd /ixigo/csgo_util/DemoParserCS2 && npm install

WORKDIR /ixigo

COPY ./start.sh start.sh
RUN sed -i -e 's/\r$//' start.sh && \
chmod +x start.sh

ENTRYPOINT ["/ixigo/start.sh"]