import { Grid, Typography } from "@mui/material";
import { useTranslation } from "react-i18next";
import { DEFAULT_SPACING } from "../../lib/constants";
import { IRconCmdsGroup, RCON_BOTS } from "./commands";
import { RCON_GAME } from "./commands/game";
import RconCommand from "./RconCommand";

const XS = 12;
const SM = 6;
const MD = 4;
const LG = 4;
const XL = 3;

const DefaultCommands = () => {
  const { t } = useTranslation();
  const cmdGroups: IRconCmdsGroup[] = [RCON_BOTS, RCON_GAME];

  return (
    <>
      {cmdGroups.map((group, i) => (
        <Grid key={i} container spacing={DEFAULT_SPACING} padding={DEFAULT_SPACING}>
          <Grid item xs={12} textAlign={"center"}>
            <Typography variant="h3">{t(`${group.languagePrefix}.title`)}</Typography>
          </Grid>
          {group.cmds.map((cmd, j) => (
            <Grid key={j} item xs={XS} sm={SM} md={MD} lg={LG} xl={XL}>
              <RconCommand cmd={cmd.cmd} image={cmd.image} label={t(`${group.languagePrefix}.buttons.${cmd.cmdKey}`)} />
            </Grid>
          ))}
        </Grid>
      ))}
    </>
  );
};

export default DefaultCommands;
