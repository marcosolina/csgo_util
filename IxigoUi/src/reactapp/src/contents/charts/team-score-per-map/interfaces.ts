import { QueryStatus } from "react-query";
import { IMapPlayed, IUserGotvScore } from "../../../services/charts";
import { ICsgoUser } from "../../../services/dem-manager";

export interface IUseTeamScorePerMapResult {
  status: QueryStatus;
  mapsPlayed: IMapPlayed[];
  matches: Record<string, IUserGotvScore[]>;
  users: ICsgoUser[];
}
