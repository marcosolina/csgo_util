import { Grid } from "@mui/material";
import { DEFAULT_SPACING } from "../../lib/constants";
import { IRconCommand } from "./interfaces";
import RconCommand from "./RconCommand";
import ct from "../../assets/bots/counterterrorist.jpg";
import terr from "../../assets/bots/terrorist.jpg";
import kick from "../../assets/bots/kickbots.jpg";

const CMDS: IRconCommand[] = [
  {
    cmd: "sm_list_players",
    name: "list the players",
    image: "",
  },
  {
    cmd: "bot_add_t",
    name: "Add Terrorist Bot",
    image: terr,
  },
  {
    cmd: "bot_add_ct",
    name: "Add C.T. Bot",
    image: ct,
  },
  {
    cmd: "bot_kick",
    name: "Kick all the bots",
    image: kick,
  },
];

const XS = 12;
const SM = 12;
const MD = 6;
const LG = 4;
const XL = 3;

const DefaultCommands = () => {
  return (
    <Grid container spacing={DEFAULT_SPACING} padding={DEFAULT_SPACING}>
      {CMDS.map((cmd, index) => (
        <Grid key={index} item xs={XS} sm={SM} md={MD} lg={LG} xl={XL}>
          <RconCommand cmd={cmd.cmd} name={cmd.name} image={cmd.image} />
        </Grid>
      ))}
    </Grid>
  );
};

export default DefaultCommands;
