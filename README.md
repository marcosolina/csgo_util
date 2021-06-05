# IxiGo Util

![Rcon UI](./misc/pictures/ixigo-logo.png)

This is a personal project that I made to simplify couple of things when playing with my friends at CSGO. We play on our CSGO [dedicated server](https://github.com/marcosolina/ixi_go) and I wanted an easier way to send RCON commands. I found [this nice library](https://github.com/Kronos666/rkon-core) which helps me to send the commands that I need. I decided then to create a simple REST API service and a simple PHP webpage to send RCON commands from the UI. In this way we just need to click on what I want to do :)

## Project Structure

![Rcon UI](./misc/pictures/Services_Diagram.png)

- csgo_util
  - [Csgo Plugins](https://github.com/marcosolina/csgo_util/tree/main/CsgoPlugins): This folder contains some custom "sourcemod" plugins written for our CSGO server
  - [CsgoRestApi](https://github.com/marcosolina/csgo_util/tree/main/CsgoRestApi): SpringBoot microservice which exposes a REST API to send the RCON commands
  - [Dem Parser](https://github.com/marcosolina/csgo_util/tree/main/DemParser): C# project created to extract information from the DEM files
  - [Discord Bot](https://github.com/marcosolina/csgo_util/tree/main/DiscordBot): SpringBoot service used to automate some task on [Discord](https://discord.com/brand-new)
  - [IxiGo Server Helper](https://github.com/marcosolina/csgo_util/tree/main/IxigoServerHelper): This Springboot app runs on the IxiGo server, it monitors the output of the custom CSGO plugins and fires the events accordingly
  - [Round Parser](https://github.com/marcosolina/csgo_util/tree/main/RoundParser): SpringBoot Project used to process the information that we extract from the dem files, and also automate some RCON commands or queries to the custom Csgo Plugins
  - **Scripts**: This folder contains scripts used to setup the project
    - **Docker**: Docker files/scripts
      - **PostgreSql**: Scripts used to setup the databases
  - [cstrike](https://marco.selfip.net/cstrike/): This folder contains all the files used to generate teh UI (PHP, Javascript, CSS)
  - **Misc**: Extra files used for documentation or the UI (CSGO Font, logo, Screenshots...)

## Requirements

- Java 11
- PHP >=5
- .NET Core
- PostgreSQL
- Spring Boot:
  - Config Server
  - Eureka Server
  - Zuul Server

## Register a CSGO Event Listener

The [CsgoRespApi](https://github.com/marcosolina/csgo_util/tree/main/CsgoRestApi) project exposes some [APIs](https://marco.selfip.net/zuul/csgo-rest-api/rcon/swagger-ui.html#/events) which allows you to register a CSGO event listener. One you have registered an event listener, that listener will receive a REST call every time one of the managed CSGO events is fired.

Supported events:

- **ROUND_END**: Triggered when the round ends (The warmup round does NOT trigger this event)
- **ROUND_START**: Triggered when a new round starts (The warmup round triggers this event)
- **CS_WIN_PANEL_MATCH**: Triggered when the match is over and the "Vote Screen" is displayed
- **WARMUP_START**: Triggered when the warmup starts
- **WARMUP_END**: This is calculated by the service, it will be fired when CSGO distpaches the first "ROUND_START" after the "WARMUP_START"

### Register an event listener

To register your event listener you have to perform an HTTP POST request to the service and provide:

- The name of the event that you want to register for
- The URL to call back when the specific event occurs.

**IMPORTANT**

- When the CSGO event is fired, your URL will receive an HTTP POST with a "content-type: application/json" and a JSON object in the body simlar to this one: "{"eventTime":"2021-05-01T19:35:32.178Z", "eventType": "ROUND_END" }"
- Your endpoint must reply with an OK HTTP Status Code (200)
- If your endpoint does not return 200 for more than three times in a row it will be "disabled". In this case you have to "un-register" and register it again

### **Examples**

**Register an event Listener**

Following is an example of registration of an event listener for the "CS_WIN_PANEL_MATCH" event

~~~~bash
curl --location --request POST 'https://marco.selfip.net/zuul/csgo-rest-api/rcon/event/register' \
--header 'Content-Type: application/json' \
--data-raw '{
    "url": "http://192.168.1.26:8080/demparser/events",
    "eventType": "CS_WIN_PANEL_MATCH"
}'
~~~~

**Event Dispatched**

Following is an example of a dispatched event from the service to your event listener

~~~~bash
curl --location --request POST 'http://192.168.1.26:8080/demparser/events' \
--header 'Content-Type: application/json' \
--data-raw '{
  "eventTime": "2021-05-01T19:35:32.178Z",
  "eventType": "CS_WIN_PANEL_MATCH"
}'
~~~~

## Misc

- [Run Chrome and allow CORS calls](https://stackoverflow.com/questions/3102819/disable-same-origin-policy-in-chrome)
