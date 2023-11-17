import { QueryStatus } from "react-query";
import { IxigoResponse } from "../../lib/http-requests";

export interface IDiscordBotConfig {
  config_key: BotConfigKey;
  config_value: string;
  config_value_type: BotConfigValueType;
}

export interface IDiscordBotConfigs {
  configs: IDiscordBotConfig[];
}

export enum BotConfigKey {
  MATCHES_TO_CONSIDER_FOR_TEAM_CREATION = "MATCHES_TO_CONSIDER_FOR_TEAM_CREATION",
  AUTOBALANCE = "AUTOBALANCE",
  KICK_BOTS = "KICK_BOTS",
  MOVE_TO_VOICE_CHANNEL = "MOVE_TO_VOICE_CHANNEL",
}

export enum BotConfigValueType {
  number = "NUMBER",
  boolean = "BOOLEAN",
}

export interface IUpdateDiscordBotConfigResult {
  saveConfig: (config: IDiscordBotConfig) => void;
  status: QueryStatus;
  response?: IxigoResponse<{}>;
}

export interface IUpdateDiscordMappedPlayersResult {
  saveMapping: (mapping: IBotMappedPlayers) => void;
  status: QueryStatus;
  response?: IxigoResponse<{}>;
}

export interface IBotMappedPlayers {
  players: IBotMappedPlayer[];
}

export interface IBotMappedPlayer {
  discord_details: IBotDiscordPlayer;
  steam_details: IBotSteamPlayer;
}

export interface IBotDiscordPlayer {
  discord_id: string;
  discord_name: string;
}

export interface IBotSteamPlayer {
  steam_id: string;
  steam_user_name: string;
}

export interface IDiscordChannelMembers {
  members: IBotDiscordPlayer[];
}

export interface ISetTeamsInVoiceChannelRequest {
  terrorist_team: IBotSteamPlayer[];
  ct_team: IBotSteamPlayer[];
}

export interface ISetTeamsInVoiceChannelResult {
  setVoiceChannel: (request: ISetTeamsInVoiceChannelRequest) => void;
  status: QueryStatus;
  response?: IxigoResponse<{}>;
}

export interface IMoveToGenericVoiceChannelResult {
  moveToGenericVoiceChannel: () => void;
  status: QueryStatus;
  response?: IxigoResponse<{}>;
}
