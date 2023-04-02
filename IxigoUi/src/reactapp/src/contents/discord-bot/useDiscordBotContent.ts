import { useSnackbar } from "notistack";
import { useEffect, useMemo, useRef } from "react";
import { useTranslation } from "react-i18next";
import { IxigoPossibleValue } from "../../common";
import { NotistackVariant } from "../../lib/constants";
import { useCheckErrorsInResponse } from "../../lib/http-requests/httpRequests";
import { combineQueryStatuses } from "../../lib/queries";
import { useGetCsgoPlayers } from "../../services";
import {
  BotConfigKey,
  IDiscordBotConfig,
  useGetDiscordBotConfig,
  useGetDiscordChannelMembers,
  useGetDiscordMappedPlayers,
  usePutDiscordBotConfig,
  usePutMappedDiscordPlayers,
} from "../../services/discord-bot";
import { IDiscordBotContentResult } from "./interfaces";
import { QueryStatus } from "../../lib/http-requests";

export const useDiscordBotContent = (): IDiscordBotContentResult => {
  const { t } = useTranslation();
  const { enqueueSnackbar } = useSnackbar();
  const q1 = useGetDiscordBotConfig(BotConfigKey.AUTOBALANCE);
  const q2 = useGetDiscordBotConfig(BotConfigKey.KICK_BOTS);
  const q3 = useGetDiscordBotConfig(BotConfigKey.MATCHES_TO_CONSIDER_FOR_TEAM_CREATION);
  const q4 = useGetDiscordBotConfig(BotConfigKey.MOVE_TO_VOICE_CHANNEL);
  const qMapping = useGetDiscordMappedPlayers();
  const qMembers = useGetDiscordChannelMembers();
  const mutateConfig = usePutDiscordBotConfig();
  const mutateMapping = usePutMappedDiscordPlayers();
  const qCsgoPlayers = useGetCsgoPlayers();

  const { checkResp } = useCheckErrorsInResponse();
  const checkRespFunc = useRef(checkResp);

  useEffect(() => {
    if (mutateConfig.status === QueryStatus.success) {
      enqueueSnackbar(t("page.discord.notifications.configSaved"), { variant: NotistackVariant.success });
    }
  }, [mutateConfig.status, t, enqueueSnackbar]);

  useEffect(() => {
    if (mutateMapping.status === QueryStatus.success) {
      enqueueSnackbar(t("page.discord.notifications.configSaved"), { variant: NotistackVariant.success });
    }
  }, [mutateMapping.status, t, enqueueSnackbar]);

  const steamUsers = useMemo((): IxigoPossibleValue[] => {
    if (!qCsgoPlayers.data?.data?.users) {
      return [];
    }

    const arr: IxigoPossibleValue[] = qCsgoPlayers.data?.data?.users.map((u) => ({
      value: u.steam_id,
      label: u.user_name,
    }));
    arr.sort((o1, o2) => o1.label.toLowerCase().localeCompare(o2.label.toLowerCase()));
    return arr;
  }, [qCsgoPlayers.data?.data?.users]);

  const members = qMembers.data?.data?.members;
  const possibleDiscordMembers = useMemo((): IxigoPossibleValue[] => {
    if (!members) {
      return [];
    }
    const arr: IxigoPossibleValue[] = members.map((m) => ({
      label: m.discord_name,
      value: m.discord_id,
    }));
    arr.sort((a, b) => a.label.localeCompare(b.label));
    return arr;
  }, [members]);

  const botConfig: IDiscordBotConfig[] = [];
  if (q1.data?.data) {
    botConfig.push(q1.data?.data);
  }
  if (q2.data?.data) {
    botConfig.push(q2.data?.data);
  }
  if (q3.data?.data) {
    botConfig.push(q3.data?.data);
  }
  if (q4.data?.data) {
    botConfig.push(q4.data?.data);
  }

  const combinedState = combineQueryStatuses([q1, q2, q3, q4, qMapping, qMembers, qCsgoPlayers]);

  useEffect(() => {
    if (q1.data) {
      checkRespFunc.current(q1.data);
    }
    if (q2.data) {
      checkRespFunc.current(q2.data);
    }
    if (q3.data) {
      checkRespFunc.current(q3.data);
    }
    if (q4.data) {
      checkRespFunc.current(q4.data);
    }
    if (qMapping.data) {
      checkRespFunc.current(qMapping.data);
    }
    if (qMembers.data) {
      checkRespFunc.current(qMembers.data);
    }
    if (qCsgoPlayers.data) {
      checkRespFunc.current(qCsgoPlayers.data);
    }
  }, [combinedState, q1.data, q2.data, q3.data, q4.data, qMapping.data, qMembers.data, qCsgoPlayers.data]);

  return {
    state: combinedState,
    steam_users: steamUsers,
    discord_channel_members: possibleDiscordMembers,
    bot_config: botConfig,
    mapped_players: qMapping.data?.data?.players || [],
    updateConfig: mutateConfig.saveConfig,
    updateConfigStatus: mutateConfig.status,
    updateMapping: mutateMapping.saveMapping,
    updateMappingStatus: mutateMapping.status,
  };
};
