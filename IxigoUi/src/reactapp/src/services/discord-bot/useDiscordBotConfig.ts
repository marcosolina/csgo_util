import { useMutation, useQuery, UseQueryResult } from "react-query";
import { SERVICES_URLS } from "../../lib/constants";
import { IxigoResponse } from "../../lib/http-requests";
import { performGet, performPut } from "../../lib/http-requests/httpRequests";
import { BotConfigKey, IDiscordBotConfig, IUpdateDiscordBotConfigResult } from "./interfaces";

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
