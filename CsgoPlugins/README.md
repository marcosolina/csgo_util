# My Plugins

This folder contains the CSGO plugins written for our games

- [SourcePawn APIs](https://sm.alliedmods.net/new-api/)
- [Introduction to SourcePawn](https://wiki.alliedmods.net/Introduction_to_SourcePawn_1.7)
- [SourcePawn IDE](https://forums.alliedmods.net/showthread.php?t=259917)
- [Metamod](https://www.metamodsource.net/downloads.php)
- [Sourcemod](https://www.sourcemod.net/downloads.php?branch=stable)

## Plugins

### MovePlayers

This plugin will move all the "input" users into the Terrorist team and the remaining will go into the CT Team.

~~~~
sm_move_players <playerName1> <playerName2> <playerName3> ... <playerNameX> dummy
~~~~

**NOTE**: I don't know why but you have to pass a "dummy" additional parameter, otherwise the plugin will not process the last player name
