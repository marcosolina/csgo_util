import { Grid, List, ListItem, ListItemText, ListSubheader } from "@mui/material";
import {
  IxigoButtonColor,
  IxigoButtonVariant,
  IxigoButtonWidth,
  IxigoMultipleSelectVariant,
  IxigoPossibleValue,
  IxigoTextState,
  IxigoTextType,
  IxigoTextVariant,
} from "../../common";
import IxigoButton from "../../common/button/IxigoButton";
import IxigoText from "../../common/input/IxigoText";
import IxigoSwitch from "../../common/switch/IxigoSwitch";
import { DEFAULT_SPACING } from "../../lib/constants";
import SaveIcon from "@mui/icons-material/Save";
import {
  BotConfigKey,
  BotConfigValueType,
  useGetDiscordBotConfig,
  useGetDiscordChannelMembers,
  useGetDiscordMappedPlayers,
} from "../../services/discord-bot";
import { useMemo } from "react";
import { useGetCsgoPlayers } from "../../services/dem-manager";
import IxigoSelect from "../../common/select/IxigoSelect";
import { useDiscordBotContent } from "./useDiscordBotContent";
import Switch from "../../common/switch-case/Switch";
import Case from "../../common/switch-case/Case";

const XS = 12;
const SM = 12;
const MD = 6;
const LG = 4;
const XL = 3;

const DiscordBotContent = () => {
  const hook = useDiscordBotContent();

  return (
    <Grid container spacing={DEFAULT_SPACING} padding={DEFAULT_SPACING}>
      <Grid item xs={XS} sm={SM} md={MD} lg={LG} xl={XL}>
        <List sx={{ width: "100%", bgcolor: "background.paper" }} subheader={<ListSubheader>Bot config</ListSubheader>}>
          <ListItem>
            {hook.bot_config.map((config) => (
              <Switch value={config.config_key} key={config.config_key}>
                <Case case={BotConfigValueType.number}>
                  <IxigoText
                    label={config.config_key}
                    value={config.config_value}
                    type={IxigoTextType.number}
                    step={1}
                  />
                </Case>
                <Case case={BotConfigValueType.boolean}>
                  <IxigoSwitch
                    checked={config.config_value === "true"}
                    value={config.config_key}
                    label={config.config_key}
                  />
                </Case>
              </Switch>
            ))}
          </ListItem>
        </List>
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
      <Grid item xs={12}></Grid>
      <Grid item xs={6}>
        <List
          sx={{ width: "100%", bgcolor: "background.paper" }}
          subheader={<ListSubheader>Discord Details</ListSubheader>}
        >
          {hook.discord_channel_members.map((player) => (
            <ListItem key={player.value}>
              <IxigoText value={player.label} variant={IxigoTextVariant.standard} state={IxigoTextState.readonly} />
            </ListItem>
          ))}
        </List>
      </Grid>
      <Grid item xs={6}>
        <List
          sx={{ width: "100%", bgcolor: "background.paper" }}
          subheader={<ListSubheader>Steam Details</ListSubheader>}
        >
          {hook.discord_channel_members.map((discord) => (
            <ListItem key={discord.value}>
              <IxigoSelect
                variant={IxigoMultipleSelectVariant.standard}
                possibleValues={hook.steam_users}
                selectedValue={
                  hook.mapped_players.find((mUser) => discord.value === mUser.discord_details.discord_id)?.steam_details
                    .steam_id || ""
                }
              />
            </ListItem>
          ))}
        </List>
      </Grid>
    </Grid>
  );
};

export default DiscordBotContent;
