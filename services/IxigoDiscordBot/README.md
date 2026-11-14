# IxiGo Discord Bot

This project manages the Discord bot that we use when we play on our IxiGo server. It is a SpringBoot based on the [JDA](https://github.com/DV8FromTheWorld/JDA) library.
This service will listen for the IXIGO events fired by the [Ixigo Server Helper](../IxigoServerHelper/) service and depending on the event it will:

- Move all the Voice users into the same channel (Game end and Warmup)
- Move all the Voice users into a separate channel (Game start)
- Generate balanced teams using the data provided by the [Ixigo Players Manager](../IxigoPlayersManager/) serivce (Warmup End)
- Move the CSGO Players to the appropriate CSGO team based on the team balance taks results (Warmup End)

## Bot Creation

- [Discord Developer Portal](https://discordapp.com/developers/): Go to the Discord developer portal and add a bot in your server
- [Discord Permission Calculator](https://discordapi.com/permissions.html): Grant the follwing permission to the bot:
  - Read Messages
  - Send Messages
  - Move Members
- Intent: Go back to the [Discord Developer Portal](https://discordapp.com/developers/), select your app and then the bot, now enable the "SERVER MEMBERS INTENT"
