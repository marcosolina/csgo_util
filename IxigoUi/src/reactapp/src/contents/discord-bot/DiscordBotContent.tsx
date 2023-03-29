import { Grid, List, ListItem, ListSubheader } from "@mui/material";
import { IxigoMultipleSelectVariant, IxigoTextState, IxigoTextType, IxigoTextVariant } from "../../common";
import IxigoText from "../../common/input/IxigoText";
import IxigoSwitch from "../../common/switch/IxigoSwitch";
import { DEFAULT_SPACING } from "../../lib/constants";
import { BotConfigValueType, IBotMappedPlayer, IDiscordBotConfig } from "../../services/discord-bot";
import { useEffect, useState } from "react";
import IxigoSelect from "../../common/select/IxigoSelect";
import { useDiscordBotContent } from "./useDiscordBotContent";
import Switch from "../../common/switch-case/Switch";
import Case from "../../common/switch-case/Case";
import { useTranslation } from "react-i18next";
import { QueryStatus } from "../../lib/http-requests";
import IxigoFloatingButton from "../../common/floating-button/IxigoFloatingButton";
import Loading from "./Loading";

const XS = 12;
const SM = 12;
const MD = 4;
const LG = 4;
const XL = 4;

let timeOut = setTimeout(() => {}, 100);

const TRANSLATIONS_BASE_PATH = "page.discord.labels";

const DiscordBotContent = () => {
  const { t } = useTranslation();
  const hook = useDiscordBotContent();
  const [botConfig, setBotConfig] = useState<IDiscordBotConfig[]>([]);
  const [mappedPlayers, setMappedPlayers] = useState<IBotMappedPlayer[]>([]);

  useEffect(() => {
    // TODO find a better way
    if (hook.state === QueryStatus.success) {
      if (botConfig.length !== hook.bot_config.length || botConfig.length === 0) {
        setBotConfig(hook.bot_config);
      }
      if (mappedPlayers.length !== hook.mapped_players.length || mappedPlayers.length === 0) {
        setMappedPlayers(hook.mapped_players);
      }
    }
  }, [hook.state, hook.bot_config, botConfig, mappedPlayers, hook.mapped_players]);

  const changeConfigHandler = (config: IDiscordBotConfig) => {
    const newConfig = [...botConfig];
    const index = newConfig.findIndex((c) => c.config_key === config.config_key);
    newConfig.splice(index, 1, config);
    setBotConfig(newConfig);
    clearTimeout(timeOut);
    timeOut = setTimeout(() => {
      hook.updateConfig(config);
    }, 300);
  };

  const changeMappedPLayer = (newValue: string, discordId: string) => {
    const newMapping = [...mappedPlayers];
    const mapping = newMapping.find((m) => m.discord_details.discord_id === discordId);
    if (mapping) {
      mapping.steam_details.steam_id = newValue;
      setMappedPlayers(newMapping);
    }
  };

  const onSaveMappingHandler = () => {
    hook.updateMapping({ players: mappedPlayers });
  };

  return (
    <Switch value={hook.state}>
      <Case case={QueryStatus.loading}>
        <Loading />
      </Case>
      <Case case={QueryStatus.success}>
        <Grid container spacing={DEFAULT_SPACING} padding={DEFAULT_SPACING}>
          <Grid item xs={XS} sm={SM} md={MD} lg={LG} xl={XL}>
            <List
              sx={{ width: "100%", bgcolor: "background.paper" }}
              subheader={<ListSubheader>{t(`${TRANSLATIONS_BASE_PATH}.config.main`)}</ListSubheader>}
            >
              {botConfig.map((config) => (
                <ListItem key={config.config_key}>
                  <Switch value={config.config_value_type}>
                    <Case case={BotConfigValueType.number}>
                      <IxigoText
                        label={t(`${TRANSLATIONS_BASE_PATH}.config.${config.config_key}`) || ""}
                        value={config.config_value}
                        type={IxigoTextType.number}
                        step={1}
                        onChange={(value) => changeConfigHandler({ ...config, config_value: value })}
                      />
                    </Case>
                    <Case case={BotConfigValueType.boolean}>
                      <IxigoSwitch
                        checked={config.config_value === "true"}
                        value={config.config_key}
                        label={t(`${TRANSLATIONS_BASE_PATH}.config.${config.config_key}`) || ""}
                        onChange={(v, checked) => changeConfigHandler({ ...config, config_value: checked.toString() })}
                      />
                    </Case>
                  </Switch>
                </ListItem>
              ))}
            </List>
          </Grid>
          <Grid item xs={6} sm={6} md={MD} lg={LG} xl={XL}>
            <List
              sx={{ width: "100%", bgcolor: "background.paper" }}
              subheader={<ListSubheader>{t(`${TRANSLATIONS_BASE_PATH}.discordUsers`)}</ListSubheader>}
            >
              {hook.discord_channel_members.map((player) => (
                <ListItem key={player.value}>
                  <IxigoText value={player.label} variant={IxigoTextVariant.standard} state={IxigoTextState.readonly} />
                </ListItem>
              ))}
            </List>
          </Grid>
          <Grid item xs={6} sm={6} md={MD} lg={LG} xl={XL}>
            <List
              sx={{ width: "100%", bgcolor: "background.paper" }}
              subheader={<ListSubheader>{t(`${TRANSLATIONS_BASE_PATH}.steamUsers`)}</ListSubheader>}
            >
              {hook.discord_channel_members.map((discord) => (
                <ListItem key={discord.value}>
                  <IxigoSelect
                    variant={IxigoMultipleSelectVariant.standard}
                    possibleValues={hook.steam_users}
                    selectedValue={
                      mappedPlayers.find((mUser) => discord.value === mUser.discord_details.discord_id)?.steam_details
                        .steam_id || ""
                    }
                    onChange={(newValue) => changeMappedPLayer(newValue, discord.value)}
                  />
                </ListItem>
              ))}
            </List>
          </Grid>
        </Grid>
        <IxigoFloatingButton
          onClick={onSaveMappingHandler}
          loading={hook.updateMappingStatus === QueryStatus.loading}
        />
      </Case>
    </Switch>
  );
};

export default DiscordBotContent;
