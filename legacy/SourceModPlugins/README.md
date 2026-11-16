# My Plugins

This folder contains the CSGO plugins written for our server

- [SourcePawn APIs](https://sm.alliedmods.net/new-api/)
- [Sourcemod Events](<https://wiki.alliedmods.net/Game_Events_(Source)>)
- [Introduction to SourcePawn](https://wiki.alliedmods.net/Introduction_to_SourcePawn_1.7)
- [SourcePawn IDE](https://forums.alliedmods.net/showthread.php?t=259917)
- [Metamod](https://www.metamodsource.net/downloads.php)
- [Sourcemod](https://www.sourcemod.net/downloads.php?branch=stable)

## Plugins

### MovePlayers

This plugin will move all the "input" users into the Terrorist team and the remaining will go into the CT Team.

```
sm_move_players <playerName1> <playerName2> <playerName3> ... <playerNameX> dummy
```

**NOTE**: I don't know why but you have to pass a "dummy" additional parameter, otherwise the plugin will not process the last player name

### LogEvent

This plugin will write in a txt file the name of the event that has occured on the server. Managed events:

- **cs_win_panel_match** Triggered when the match is over and the "Vote Screen" is displayed
- **round_start**: Triggered when a new round starts (The warmup round triggers this event)
- **round_end**: Triggered when the round ends (The warmup round does NOT trigger this event)

### List Players

It print the steam ID of the currently active players on the server

```
sm_list_players

# Output
TERRORISTS
123
456
CT
789
321
```

## Misc

- [Return to normal warmup in competitive mode](https://github.com/Ilusion9/fix-warmup-csgo)
