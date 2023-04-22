import { QueryStatus } from "react-query";
import { IAvgScoresPerMap } from "../../../services/charts";
import { ICsgoUser } from "../../../services/dem-manager";
import { IxigoPossibleValue } from "../../../common/select";

export interface IUseAvgScoresPerMapDataRequest {
  steamIds: string[];
  scoreType: string;
}

export interface IUseAvgScoresPerMapDataResult {
  status: QueryStatus;
  users: ICsgoUser[];
  avgScores?: IAvgScoresPerMap;
  scoreTypes: IxigoPossibleValue[];
}
