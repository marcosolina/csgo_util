import { QueryStatus } from "react-query";
import { IxigoResponse } from "../../lib/http-requests";

export interface IGetTeamsRequest {
  steamIDs: string[];
  numberOfMatches?: number;
  penaltyWeigth?: number;
  partitionScore?: string;
  minPercPlayed?: number;
}

export interface IGetTeamsResponse {
  teams: ITeam[];
}

export interface ITeam {
  team_score: number;
  team_members: ITeamPlayer[];
}

export interface ITeamPlayer {
  steam_id: string;
  user_name: string;
  split_score: number;
  original_split_score: number;
}

export interface IGetTeamsResult {
  getTeams: (request: IGetTeamsRequest) => void;
  status: QueryStatus;
  response?: IxigoResponse<IGetTeamsResponse>;
}