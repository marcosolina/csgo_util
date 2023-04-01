import { useMutation, useQuery, UseQueryResult } from "react-query";
import { SERVICES_URLS } from "../../lib/constants";
import { IxigoResponse } from "../../lib/http-requests";
import { performGet, performPut } from "../../lib/http-requests/httpRequests";
import {
  BotConfigKey,
  IBotMappedPlayers,
  IDiscordBotConfig,
  IDiscordChannelMembers,
  IUpdateDiscordBotConfigResult,
  IUpdateDiscordMappedPlayersResult,
} from "./interfaces";

export const useGetDiscordBotConfig = (
  configKey: BotConfigKey
): UseQueryResult<IxigoResponse<IDiscordBotConfig>, unknown> => {
  return useQuery(
    ["getDiscordBotConfig", configKey],
    async () => await performGet<IDiscordBotConfig>(`${SERVICES_URLS["discord-bot"]["get-config"]}/${configKey}`)
  );
};

export const usePutDiscordBotConfig = (): IUpdateDiscordBotConfigResult => {
  const mutation = useMutation(async (config: IDiscordBotConfig) => {
    return await performPut<{}, IDiscordBotConfig>(`${SERVICES_URLS["discord-bot"]["put-config"]}`, config);
  });

  return {
    saveConfig: mutation.mutate,
    status: mutation.status,
    response: mutation.data,
  };
};

export const usePutMappedDiscordPlayers = (): IUpdateDiscordMappedPlayersResult => {
  const mutation = useMutation(async (mapping: IBotMappedPlayers) => {
    return await performPut<{}, IBotMappedPlayers>(`${SERVICES_URLS["discord-bot"]["put-mapped-players"]}`, mapping);
  });

  return {
    saveMapping: mutation.mutate,
    status: mutation.status,
    response: mutation.data,
  };
};

export const useGetDiscordMappedPlayers = (): UseQueryResult<IxigoResponse<IBotMappedPlayers>, unknown> => {
  return useQuery(
    ["getDiscordMappedPlayers"],
    async () => await performGet<IBotMappedPlayers>(`${SERVICES_URLS["discord-bot"]["get-mapped-players"]}`)
  );
};

export const useGetDiscordChannelMembers = (): UseQueryResult<IxigoResponse<IDiscordChannelMembers>, unknown> => {
  return useQuery(
    ["getDiscordChannelMembers"],
    async () =>
      await performGet<IDiscordChannelMembers>(`${SERVICES_URLS["discord-bot"]["get-discord-channel-members"]}`)
  );
};
