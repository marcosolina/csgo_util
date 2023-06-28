import { useSnackbar } from "notistack";
import { useEffect, useMemo, useRef } from "react";
import { useTranslation } from "react-i18next";
import { IxigoPossibleValue } from "../../common";
import { NotistackVariant } from "../../lib/constants";
import { useCheckErrorsInResponse } from "../../lib/http-requests/httpRequests";
import { combineQueryStatuses } from "../../lib/queries";
import { useGetCsgoPlayers } from "../../services";
import {
  useGetDiscordBotConfigAll,
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
  const qAllConfigs = useGetDiscordBotConfigAll();
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
      qAllConfigs.refetch();
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

  const combinedState = combineQueryStatuses([qMapping, qMapping, qMembers, qCsgoPlayers]);

  useEffect(() => {
    if (qMapping.data) {
      checkRespFunc.current(qMapping.data);
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
  }, [combinedState, qMapping.data, qMembers.data, qCsgoPlayers.data]);

  return {
    state: combinedState,
    steam_users: steamUsers,
    discord_channel_members: possibleDiscordMembers,
    bot_config: qAllConfigs.data?.data?.configs || [],
    mapped_players: qMapping.data?.data?.players || [],
    updateConfig: mutateConfig.saveConfig,
    updateConfigStatus: mutateConfig.status,
    updateMapping: mutateMapping.saveMapping,
    updateMappingStatus: mutateMapping.status,
  };
};
