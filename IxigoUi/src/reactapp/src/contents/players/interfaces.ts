import { QueryStatus } from "react-query";
import { ICsgoUser } from "../../services";
import { ITeam } from "../../services/players-manager";

export interface IPlayersContent {
  state: QueryStatus;
  scoreTypes?: Record<string, string>;
  csgoPlayers?: ICsgoUser[];
}

export interface IIxigoTeamProps {
  picture: string;
  title: string;
  team?: ITeam;
  status: QueryStatus;
}
