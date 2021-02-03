# IxiGo Util

![Rcon UI](./misc/pictures/ixigo-logo.png)

This is a personal project that I made to simplify couple of things when playing with my friends at CSGO. We play on our CSGO [dedicated server](https://github.com/marcosolina/csgo_server) and I wanted an easier way to send RCON commands. I found [this nice library](https://github.com/Kronos666/rkon-core) which helps me to send the commands that I need. I decided then to create a simple REST API service and a simple PHP webpage to send RCON commands from the UI. In this way we just need to click on what I want to do :)

## Project Structure

- csgo_util
  - CsgoRespApi: SpringBoot microservice which exposes a REST API to send the RCON commands
  - cstrike: This folder contains all the files used to generate teh UI (PHP, Javascript, CSS)
  ![Rcon UI](./misc/pictures/rconui.png)
  - Misc: Extra files used for documentation or the UI (CSGO Font, logo, Screenshots...)

## Requirements

- Java 11
- PHP >=5
