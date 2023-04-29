import { QueryStatus } from "react-query";
import { IAvgPlayersScoresPerMap, IMapPlayed } from "../../../services/charts";
import { ICsgoUser } from "../../../services/dem-manager";
import { IxigoPossibleValue } from "../../../common/select";

export interface IUsePlayersAvgScoresPerMapDataRequest {
  steamIds: string[];
  maps: string[];
  matchesToConsider: string;
  scoreType: string;
}

export interface IUsePlayersAvgScoresPerMapDataResult {
  status: QueryStatus;
  users: ICsgoUser[];
  scoreTypes: IxigoPossibleValue[];
  mapsPlayed: IMapPlayed[];

  avgScores?: IAvgPlayersScoresPerMap;
}
