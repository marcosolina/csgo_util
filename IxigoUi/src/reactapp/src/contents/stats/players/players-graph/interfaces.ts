import { QueryStatus } from "react-query";
import { IxigoPossibleValue } from "../../../../common/select";
import { ChartDataset } from "chart.js";

export interface IGraphContentProps {
  steamid: string;
}

export enum BinningLevel {
  week = "week",
  month = "month",
}

export interface IChartData {
  labels: string[];
  datasets: ChartDataset<"line", number[]>[];
}

export interface IUsePlayersGraphContentResult {
  state: QueryStatus;
  startDate: Date | null;
  endDate: Date | null;
  binningLevel?: string;
  graphsSelected?: string[];
  possibleScoreTypesValues: IxigoPossibleValue[];
  possibleBinningValues: IxigoPossibleValue[];
  chartsData: IChartData[];

  setSteamId: (val?: string) => void;
  setStartDate?: (val: Date | null) => void;
  setEndDate?: (val: Date | null) => void;
  setBinningLevel?: (val: string) => void;
  setGraphsSelected?: (val: string[]) => void;
}
