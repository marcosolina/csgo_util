import { QueryStatus } from "react-query";
import { IxigoResponse } from "../../lib/http-requests";

export interface IDiscordBotConfig {
  config_key: BotConfigKey;
  config_value: string;
}

export enum BotConfigKey {
  ROUNDS_TO_CONSIDER_FOR_TEAM_CREATION = "ROUNDS_TO_CONSIDER_FOR_TEAM_CREATION",
  AUTOBALANCE = "AUTOBALANCE",
  KICK_BOTS = "KICK_BOTS",
}

export interface IUpdateDiscordBotConfigResult {
  saveConfig: (config: IDiscordBotConfig) => void;
  status: QueryStatus;
  response?: IxigoResponse<{}>;
}
