import { Grid, List, ListItem, ListSubheader } from "@mui/material";
import { IxigoButtonColor, IxigoButtonVariant, IxigoButtonWidth, IxigoTextType } from "../../common";
import IxigoButton from "../../common/button/IxigoButton";
import IxigoText from "../../common/input/IxigoText";
import IxigoSwitch from "../../common/switch/IxigoSwitch";
import { DEFAULT_SPACING } from "../../lib/constants";
import SaveIcon from "@mui/icons-material/Save";
import { BotConfigKey, useGetDiscordBotConfig } from "../../services/discord-bot";

const XS = 12;
const SM = 12;
const MD = 6;
const LG = 4;
const XL = 3;

const DiscordBotContent = () => {
  const q1 = useGetDiscordBotConfig(BotConfigKey.AUTOBALANCE);
  const q2 = useGetDiscordBotConfig(BotConfigKey.KICK_BOTS);
  const q3 = useGetDiscordBotConfig(BotConfigKey.ROUNDS_TO_CONSIDER_FOR_TEAM_CREATION);
  return (
    <Grid container spacing={DEFAULT_SPACING} padding={DEFAULT_SPACING}>
      <Grid item xs={XS} sm={SM} md={MD} lg={LG} xl={XL}>
        <List sx={{ width: "100%", bgcolor: "background.paper" }} subheader={<ListSubheader>Flags</ListSubheader>}>
          <ListItem>
            <IxigoSwitch
              checked={q2.data?.data?.config_value === "true"}
              value={q2.data?.data?.config_key as string}
              label={q2.data?.data?.config_key}
            />
          </ListItem>
          <ListItem>
            <IxigoSwitch
              checked={q1.data?.data?.config_value === "true"}
              value={q1.data?.data?.config_key as string}
              label={q1.data?.data?.config_key}
            />
          </ListItem>
        </List>
      </Grid>
      <Grid item xs={XS} sm={SM} md={MD} lg={LG} xl={XL}>
        <IxigoText
          label={"Rounds to consider"}
          value={q3.data?.data?.config_value}
          type={IxigoTextType.number}
          step={1}
        />
      </Grid>
      <Grid item xs={XS} sm={SM} md={MD} lg={LG} xl={XL}>
        <IxigoButton
          text={
            <>
              Save the mapping&nbsp;
              <SaveIcon />
            </>
          }
          width={IxigoButtonWidth.fitParent}
          variant={IxigoButtonVariant.outlined}
          color={IxigoButtonColor.primary}
        />
      </Grid>
    </Grid>
  );
};

export default DiscordBotContent;
