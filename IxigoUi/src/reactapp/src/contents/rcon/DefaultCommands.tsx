import { Grid } from "@mui/material";
import { DEFAULT_SPACING } from "../../lib/constants";
import RconCommand from "./RconCommand";

const CMDS = [
  {
    cmd: "sm_list_players",
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
          <RconCommand />
        </Grid>
      ))}
    </Grid>
  );
};

export default DefaultCommands;
