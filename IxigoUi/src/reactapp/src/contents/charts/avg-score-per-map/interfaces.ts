import { QueryStatus } from "react-query";
import { IAvgScoresPerMap, IMapPlayed } from "../../../services/charts";
import { ICsgoUser } from "../../../services/dem-manager";
import { IxigoPossibleValue } from "../../../common/select";

export interface IUseAvgScoresPerMapDataRequest {
  steamIds: string[];
  maps: string[];
  matchesToConsider: string;
  scoreType: string;
}

export interface IUseAvgScoresPerMapDataResult {
  status: QueryStatus;
  users: ICsgoUser[];
  scoreTypes: IxigoPossibleValue[];
  mapsPlayed: IMapPlayed[];

  avgScores?: IAvgScoresPerMap;
}
