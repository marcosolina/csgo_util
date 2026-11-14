# Ixigo Plugin

These plugins is used to export CS2 events and to move the players to a different team.
These plugins are based on the [Counter Strike Sharp](https://docs.cssharp.dev/index.html) framweork.

## Installation

- Donwload [MetaMod](https://www.sourcemm.net/downloads.php?branch=dev)
- Download the runtime build from the [git hub release page](https://github.com/roflmuffin/CounterStrikeSharp/releases)
- Unzip metamod and copy the `addons` folder to the game/csgo directory on the server
- Do the same for the runtime build
- In the game/csgo there is a gameinfo.gi file, open it with a text editor and add the line "Game csgo/addons/metamod" after the line "Game_LowViolence csgo_lv", it should look like this:
  ```
  Game_LowViolence	csgo_lv // Perfect World content override
  Game	csgo/addons/metamod
  ```

[Video Tutorial](https://www.youtube.com/watch?v=FlsKzStHJuY)

## Commands

- `meta`: Shows the metamod menu
- `meta list`: Lists all the plugins
- `css_plugins list`: Lists all the plugins
