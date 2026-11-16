import { Card, CardContent, Grid, List, ListItem, ListSubheader, Typography } from "@mui/material";
import {
  IxigoButtonColor,
  IxigoButtonType,
  IxigoButtonVariant,
  IxigoSelectVariant,
  IxigoTextState,
  IxigoTextType,
  IxigoTextVariant,
} from "../../common";
import IxigoText from "../../common/input/IxigoText";
import IxigoSwitch from "../../common/switch/IxigoSwitch";
import { DEFAULT_SPACING } from "../../lib/constants";
import {
  BotConfigValueType,
  IBotMappedPlayer,
  IDiscordBotConfig,
  useMakeTeamsWithUsersInVoiceChannelAndMove,
  useMoveToGenericVoiceChannel,
} from "../../services/discord-bot";
import { useCallback, useEffect, useMemo, useState } from "react";
import IxigoSelect from "../../common/select/IxigoSelect";
import { useDiscordBotContent } from "./useDiscordBotContent";
import Switch from "../../common/switch-case/Switch";
import Case from "../../common/switch-case/Case";
import { useTranslation } from "react-i18next";
import { QueryStatus } from "../../lib/http-requests";
import IxigoFloatingButton from "../../common/floating-button/IxigoFloatingButton";
import Loading from "./Loading";
import SafetyDividerIcon from "@mui/icons-material/SafetyDivider";
import GroupsIcon from "@mui/icons-material/Groups";
import LocalFireDepartmentIcon from "@mui/icons-material/LocalFireDepartment";
import { IAction } from "./interfaces";
import IxigoButton from "../../common/button/IxigoButton";
import { useSendEventCommand } from "../../services/event-dispatcher";

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
  const { moveToGenericVoiceChannel, status: moveToGenericStatus } = useMoveToGenericVoiceChannel();
  const { makeTeamsAndMoveToVoice, status: mokeAndMoveStatus } = useMakeTeamsWithUsersInVoiceChannelAndMove();
  const { sendEvent, status: eventStatus } = useSendEventCommand();
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

  const sendEvt = useCallback(
    (event: string) => {
      sendEvent({ event_name: event });
    },
    [sendEvent]
  );

  const actions: IAction[] = useMemo(() => {
    return [
      {
        icon: <SafetyDividerIcon />,
        name: t(`${TRANSLATIONS_BASE_PATH}.lblBtnMoveToVoiceChannel`) as string,
        onClick: makeTeamsAndMoveToVoice,
        loading: mokeAndMoveStatus === QueryStatus.loading,
      },
      {
        icon: <GroupsIcon />,
        name: t(`${TRANSLATIONS_BASE_PATH}.lblBtnMoveToGenericVoiceChannel`) as string,
        onClick: moveToGenericVoiceChannel,
        loading: moveToGenericStatus === QueryStatus.loading,
      },
    ];
  }, [t, moveToGenericVoiceChannel, moveToGenericStatus, makeTeamsAndMoveToVoice, mokeAndMoveStatus]);

  return (
    <Switch value={hook.state}>
      <Case case={QueryStatus.loading}>
        <Loading />
      </Case>
      <Case case={QueryStatus.success}>
        <Grid container spacing={DEFAULT_SPACING} padding={DEFAULT_SPACING}>
          <Grid item xs={XS}>
            <Card>
              <CardContent>
                <Typography color="text.secondary" align="center">
                  {t(`${TRANSLATIONS_BASE_PATH}.actions.title`)}
                </Typography>
                <Grid container spacing={DEFAULT_SPACING}>
                  <Grid item xs={XS} sm={SM} md={MD} lg={LG} xl={XL}>
                    <Card variant="outlined">
                      <CardContent>
                        <Typography color="text.secondary" gutterBottom>
                          {t(`${TRANSLATIONS_BASE_PATH}.actions.lblVoiceChannelActions`)}
                        </Typography>
                        {actions.map((action) => (
                          <IxigoButton
                            key={action.name}
                            text={action.icon}
                            toolTip={action.name}
                            onClick={action.onClick}
                            color={IxigoButtonColor.primary}
                            variant={IxigoButtonVariant.outlined}
                            loading={action.loading}
                            type={IxigoButtonType.justicon}
                          />
                        ))}
                      </CardContent>
                    </Card>
                  </Grid>
                  <Grid item xs={XS} sm={SM} md={MD} lg={LG} xl={XL}>
                    <Card variant="outlined">
                      <CardContent>
                        <Typography color="text.secondary" gutterBottom>
                          {t(`${TRANSLATIONS_BASE_PATH}.actions.lblTriggerEvents`)}
                        </Typography>
                        <IxigoButton
                          key={"Test"}
                          text={<LocalFireDepartmentIcon />}
                          toolTip={t(`${TRANSLATIONS_BASE_PATH}.lblBtnSendWarmupStartEvent`) as string}
                          onClick={() => sendEvt("round_announce_warmup")}
                          color={IxigoButtonColor.primary}
                          variant={IxigoButtonVariant.outlined}
                          loading={eventStatus === QueryStatus.loading}
                          type={IxigoButtonType.justicon}
                        />
                      </CardContent>
                    </Card>
                  </Grid>
                </Grid>
              </CardContent>
            </Card>
          </Grid>
          <Grid item xs={XS} sm={SM} md={MD} lg={LG} xl={XL}>
            <Card>
              <CardContent>
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
                            onChange={(v, checked) =>
                              changeConfigHandler({ ...config, config_value: checked.toString() })
                            }
                          />
                        </Case>
                      </Switch>
                    </ListItem>
                  ))}
                </List>
              </CardContent>
            </Card>
          </Grid>
          <Grid item xs={XS} sm={SM} md={8} lg={8} xl={8}>
            <Card>
              <CardContent>
                <Grid container>
                  <Grid item xs={6}>
                    <List
                      sx={{ width: "100%", bgcolor: "background.paper" }}
                      subheader={<ListSubheader>{t(`${TRANSLATIONS_BASE_PATH}.discordUsers`)}</ListSubheader>}
                    >
                      {hook.discord_channel_members.map((player) => (
                        <ListItem key={player.value}>
                          <IxigoText
                            value={player.label}
                            variant={IxigoTextVariant.standard}
                            state={IxigoTextState.readonly}
                          />
                        </ListItem>
                      ))}
                    </List>
                  </Grid>
                  <Grid item xs={6}>
                    <List
                      sx={{ width: "100%", bgcolor: "background.paper" }}
                      subheader={<ListSubheader>{t(`${TRANSLATIONS_BASE_PATH}.steamUsers`)}</ListSubheader>}
                    >
                      {hook.discord_channel_members.map((discord) => (
                        <ListItem key={discord.value}>
                          <IxigoSelect
                            variant={IxigoSelectVariant.standard}
                            possibleValues={hook.steam_users}
                            selectedValue={
                              mappedPlayers.find((mUser) => discord.value === mUser.discord_details.discord_id)
                                ?.steam_details.steam_id || ""
                            }
                            onChange={(newValue) => changeMappedPLayer(newValue, discord.value)}
                          />
                        </ListItem>
                      ))}
                    </List>
                  </Grid>
                </Grid>
              </CardContent>
            </Card>
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
