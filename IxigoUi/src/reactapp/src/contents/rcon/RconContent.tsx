import Grid from "@mui/material/Grid";
import { IxigoTextState, IxigoTextType } from "../../common/input";
import IxigoText from "../../common/input/IxigoText";
import { DEFAULT_SPACING } from "../../lib/constants";
import SendIcon from "@mui/icons-material/Send";
import { IconButton, InputAdornment } from "@mui/material";
import { useState } from "react";
import { IRconRquest } from "../../services";

const XS = 12;
const SM = 12;
const MD = 6;
const LG = 4;
const XL = 3;

const RconContent = () => {
  const [request, setRequest] = useState<IRconRquest>();

  return (
    <Grid container spacing={DEFAULT_SPACING} padding={DEFAULT_SPACING}>
      <Grid item xs={XS} sm={SM} md={MD} lg={LG} xl={XL}>
        <IxigoText label={"RCON Server Address"} value={request?.rcon_host} state={IxigoTextState.mandatory} />
      </Grid>
      <Grid item xs={XS} sm={SM} md={MD} lg={LG} xl={XL}>
        <IxigoText
          label={"RCON Server Port"}
          value={request?.rcon_port as string | undefined}
          state={IxigoTextState.mandatory}
          type={IxigoTextType.number}
        />
      </Grid>
      <Grid item xs={XS} sm={SM} md={MD} lg={LG} xl={XL}>
        <IxigoText label={"RCON Server Password"} value={request?.rcon_passw} state={IxigoTextState.mandatory} />
      </Grid>
      <Grid item xs={XS} sm={SM} md={MD} lg={LG} xl={XL}>
        <IxigoText
          label={"RCON command to send"}
          value={request?.rcon_command}
          state={IxigoTextState.mandatory}
          endAdornment={
            <InputAdornment position="end">
              <IconButton aria-label="toggle password visibility" edge="end" color="primary">
                <SendIcon />
              </IconButton>
            </InputAdornment>
          }
        />
      </Grid>
    </Grid>
  );
};

export default RconContent;
