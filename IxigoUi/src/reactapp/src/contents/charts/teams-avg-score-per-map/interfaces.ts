import { QueryStatus } from "react-query";
import { IAvgTeamsScoresPerMap, IMapPlayed } from "../../../services/charts";
import { IxigoPossibleValue } from "../../../common/select";

export interface IUseTeamsAvgScoresPerMapDataResult {
  status: QueryStatus;
  scoreTypes: IxigoPossibleValue[];
  mapsPlayed: IMapPlayed[];
  avgScores?: IAvgTeamsScoresPerMap;
}
