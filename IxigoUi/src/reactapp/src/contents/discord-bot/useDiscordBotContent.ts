import { useMemo } from "react";
import { IxigoPossibleValue } from "../../common";
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
  const p = useGetDiscordMappedPlayers();
  const p2 = useGetDiscordChannelMembers();
  const qCsgoPlayers = useGetCsgoPlayers();

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

  const possibleDiscordMembers = useMemo((): IxigoPossibleValue[] => {
    if (!p2.data?.data?.members) {
      return [];
    }

    const arr: IxigoPossibleValue[] = p2.data?.data?.members.map((m) => ({
      label: m.discord_name,
      value: m.discord_id,
    }));
    arr.sort((a, b) => a.label.localeCompare(b.label));
    return arr;
  }, [!p2.data?.data?.members]);

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

  return {
    state: combineQueryStatuses([q1.status, q2.status, q3.status, p.status, p2.status, qCsgoPlayers.status]),
    steam_users: steamUsers,
    discord_channel_members: possibleDiscordMembers,
    bot_config: botConfig,
  };
};
