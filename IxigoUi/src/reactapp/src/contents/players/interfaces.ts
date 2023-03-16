import { QueryStatus } from "react-query";
import { ICsgoUser } from "../../services";

export interface IPlayersContent {
  state: QueryStatus;
  scoreTypes?: Record<string, string>;
  csgoPlayers?: ICsgoUser[];
}
