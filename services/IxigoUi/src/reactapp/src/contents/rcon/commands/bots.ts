import ct from "../../../assets/bots/counterterrorist.jpg";
import terr from "../../../assets/bots/terrorist.jpg";
import kick from "../../../assets/bots/kickbots.jpg";
import { IRconCmdsGroup } from ".";

export const RCON_BOTS: IRconCmdsGroup = {
  languagePrefix: "page.rcon.commands.groups.bots",
  cmds: [
    {
      cmd: "bot_add_t",
      cmdKey: "bot_add_t",
      image: terr,
    },
    {
      cmd: "bot_add_ct",
      cmdKey: "bot_add_ct",
      image: ct,
    },
    {
      cmd: "bot_kick",
      cmdKey: "bot_kick",
      image: kick,
    },
  ],
};
