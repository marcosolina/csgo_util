import { IRconCmdsGroup } from "./interfaces";

export const RCON_GAME: IRconCmdsGroup = {
  languagePrefix: "page.rcon.commands.groups.game",
  cmds: [
    {
      cmd: "sm_list_players",
      cmdKey: "sm_list_players",
    },
    {
      cmd: "mp_restartgame 5",
      cmdKey: "mp_restartgame",
    },
    {
      cmd: "pause",
      cmdKey: "pause",
    },
    {
      cmd: "unpause",
      cmdKey: "unpause",
    },
    {
      cmd: "exit",
      cmdKey: "exit",
    },
  ],
};
