import { QueryStatus } from "react-query";
import { IxigoPossibleValue } from "../../common/select";
import { IDiscordBotConfig } from "../../services/discord-bot";

export interface IDiscordBotContentResult {
  state: QueryStatus;
  steam_users: IxigoPossibleValue[];
  discord_channel_members: IxigoPossibleValue[];
  bot_config: IDiscordBotConfig[];
}
