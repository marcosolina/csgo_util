import { QueryStatus } from "react-query";

export interface IPlayerRadarChartProps {
  steamid: string;
}

export interface IPlayerRadarChartRequest {
  steamid: string;
}

export interface IPlayerRadarChartData {
  kprZScore: number;
  hltv_ratingZScore: number;
  adrZScore: number;
  kastZScore: number;
  dprZScore: number;
  hspZScore: number;
  udZScore: number;
  ebtZScore: number;
}

export interface IPlayerRadarChartResponse {
  state: QueryStatus;
  chartData?: IPlayerRadarChartData;
  originalData?: IPlayerRadarChartData;
}
