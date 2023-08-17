import { QueryStatus } from "react-query";
import { IxigoPossibleValue } from "../../../../common/select";
import { ITeamMatchResults } from "../../../../services";

export interface IGraphContentProps {
  steamid: string;
}

export enum BinningLevel {
  week = "week",
  month = "month",
}

export interface IChartData {
  label: string;
  data: number[];
}

export interface IUsePlayersGraphContentResult {
  state: QueryStatus;
  startDate: Date | null;
  endDate: Date | null;
  binningLevel?: string;
  graphsSelected?: string[];
  possibleScoreTypesValues: IxigoPossibleValue[];
  possibleBinningValues: IxigoPossibleValue[];
  chartData: IChartData[];

  setSteamId: (val?: string) => void;
  setStartDate?: (val: Date | null) => void;
  setEndDate?: (val: Date | null) => void;
  setBinningLevel?: (val: string) => void;
  setGraphsSelected?: (val: string[]) => void;
}
