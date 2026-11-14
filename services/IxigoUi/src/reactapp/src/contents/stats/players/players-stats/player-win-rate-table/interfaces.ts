import { MRT_ColumnDef } from "material-react-table";
import { QueryStatus } from "react-query";
import { IPlayerOverallStats } from "../../../../../services/stats";

export interface IWinRateData {
  desc: string;
  value: string;
}

export interface IPlayerWinRateTableProps {
  steamid: string;
}

export interface IPlayerWinRateRequest {
  steamid: string;
}

export interface IPlayerWinRateResponse {
  state: QueryStatus;
  columns: MRT_ColumnDef<IWinRateData>[];
  data: IWinRateData[];
  playerOverall?: IPlayerOverallStats;
}
