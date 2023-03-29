import { QueryStatus } from "react-query";
import { IxigoPossibleValue } from "../../common/select";
import { IBotMappedPlayer, IBotMappedPlayers, IDiscordBotConfig } from "../../services/discord-bot";

export interface IDiscordBotContentResult {
  state: QueryStatus;
  steam_users: IxigoPossibleValue[];
  discord_channel_members: IxigoPossibleValue[];
  bot_config: IDiscordBotConfig[];
  mapped_players: IBotMappedPlayer[];
  updateConfig: (config: IDiscordBotConfig) => void;
  updateConfigStatus: QueryStatus;
  updateMapping: (mapping: IBotMappedPlayers) => void;
  updateMappingStatus: QueryStatus;
}
