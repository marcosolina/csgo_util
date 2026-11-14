import { useMutation, useQuery, UseQueryResult } from "react-query";
import { NotistackVariant, SERVICES_URLS } from "../../lib/constants";
import { IxigoResponse } from "../../lib/http-requests";
import { performGet, performPost, performPut } from "../../lib/http-requests/httpRequests";
import {
  BotConfigKey,
  IBotMappedPlayers,
  IDiscordBotConfig,
  IDiscordBotConfigs,
  IDiscordChannelMembers,
  IMoveToGenericVoiceChannelResult,
  IMakeTeamsWithUsersInVoiceChannelAndMoveResult,
  IUpdateDiscordBotConfigResult,
  IUpdateDiscordMappedPlayersResult,
} from "./interfaces";
import { useSnackbar } from "notistack";
import { useTranslation } from "react-i18next";

const TRANSLATIONS_BASE_PATH = "services.discordbot.messages";

export const useGetDiscordBotConfig = (
  configKey: BotConfigKey
): UseQueryResult<IxigoResponse<IDiscordBotConfig>, unknown> => {
  return useQuery(
    ["getDiscordBotConfig", configKey],
    async () => await performGet<IDiscordBotConfig>(`${SERVICES_URLS["discord-bot"]["get-config"]}/${configKey}`)
  );
};

export const useGetDiscordBotConfigAll = (): UseQueryResult<IxigoResponse<IDiscordBotConfigs>, unknown> => {
  return useQuery(
    ["getDiscordBotConfigAll"],
    async () => await performGet<IDiscordBotConfigs>(`${SERVICES_URLS["discord-bot"]["get-config-all"]}`)
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

export const useMakeTeamsWithUsersInVoiceChannelAndMove = (): IMakeTeamsWithUsersInVoiceChannelAndMoveResult => {
  const { enqueueSnackbar } = useSnackbar();
  const { t } = useTranslation();

  const mutation = useMutation(
    async () => {
      return await performPost<{}, {}>(SERVICES_URLS["discord-bot"]["post-make-teams-and-move-to-voice-channel"], {});
    },
    {
      onSuccess: (data: any) => {
        console.log(data);
        enqueueSnackbar(t(`${TRANSLATIONS_BASE_PATH}.moveToVoiceChannel.success`), {
          variant: NotistackVariant.success,
        });
      },
    }
  );

  return {
    makeTeamsAndMoveToVoice: mutation.mutate,
    status: mutation.status,
    response: mutation.data,
  };
};

export const useMoveToGenericVoiceChannel = (): IMoveToGenericVoiceChannelResult => {
  const { enqueueSnackbar } = useSnackbar();
  const { t } = useTranslation();

  const mutation = useMutation(
    async () => {
      return await performPost<{}, {}>(SERVICES_URLS["discord-bot"]["post-move-to-general-voice-channel"], {});
    },
    {
      onSuccess: (data) => {
        console.log(data);
        enqueueSnackbar(t(`${TRANSLATIONS_BASE_PATH}.moveToGenericVoiceChannel.success`), {
          variant: NotistackVariant.success,
        });
      },
    }
  );

  return {
    moveToGenericVoiceChannel: mutation.mutate,
    status: mutation.status,
    response: mutation.data,
  };
};
