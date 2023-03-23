import { useEffect, useMemo, useRef } from "react";
import { IxigoPossibleValue } from "../../common";
import { useCheckErrorsInResponse } from "../../lib/http-requests/httpRequests";
import { combineQueryStatuses } from "../../lib/queries";
import { useGetCsgoPlayers } from "../../services";
import {
  BotConfigKey,
  IDiscordBotConfig,
  useGetDiscordBotConfig,
  useGetDiscordChannelMembers,
  useGetDiscordMappedPlayers,
} from "../../services/discord-bot";
import { IDiscordBotContentResult } from "./interfaces";

export const useDiscordBotContent = (): IDiscordBotContentResult => {
  const q1 = useGetDiscordBotConfig(BotConfigKey.AUTOBALANCE);
  const q2 = useGetDiscordBotConfig(BotConfigKey.KICK_BOTS);
  const q3 = useGetDiscordBotConfig(BotConfigKey.ROUNDS_TO_CONSIDER_FOR_TEAM_CREATION);
  const qMapping = useGetDiscordMappedPlayers();
  const qMembers = useGetDiscordChannelMembers();
  const qCsgoPlayers = useGetCsgoPlayers();

  const { checkResp } = useCheckErrorsInResponse();
  const checkRespFunc = useRef(checkResp);

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

  const combinedState = combineQueryStatuses([
    q1.status,
    q2.status,
    q3.status,
    qMapping.status,
    qMembers.status,
    qCsgoPlayers.status,
  ]);

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
    if (qMapping.data) {
      checkRespFunc.current(qMapping.data);
    }
    if (qMembers.data) {
      checkRespFunc.current(qMembers.data);
    }
    if (qCsgoPlayers.data) {
      checkRespFunc.current(qCsgoPlayers.data);
    }
  }, [combinedState, q1.data, q2.data, q3.data, qMapping.data, qMembers.data, qCsgoPlayers.data]);

  return {
    state: combineQueryStatuses([
      q1.status,
      q2.status,
      q3.status,
      qMapping.status,
      qMembers.status,
      qCsgoPlayers.status,
    ]),
    steam_users: steamUsers,
    discord_channel_members: possibleDiscordMembers,
    bot_config: botConfig,
    mapped_players: qMapping.data?.data?.players || [],
  };
};
