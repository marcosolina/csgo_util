import { QueryStatus } from "react-query";
import { IxigoPossibleValue } from "../../../../common/select";

export interface IGraphContentProps {
  steamid: string;
}

export interface IUsePlayersGraphContentResult {
  state: QueryStatus;
  startDate: Date | null;
  endDate: Date | null;
  binningLevel?: string;
  graphsSelected?: string[];
  possibleScoreTypesValues: IxigoPossibleValue[];
  possibleBinningValues: IxigoPossibleValue[];

  setStartDate?: (val: Date | null) => void;
  setEndDate?: (val: Date | null) => void;
  setBinningLevel?: (val?: string) => void;
  setGraphsSelected?: (val?: string[]) => void;
}
