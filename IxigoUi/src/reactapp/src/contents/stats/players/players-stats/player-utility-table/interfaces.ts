import { MRT_ColumnDef } from "material-react-table";
import { QueryStatus } from "react-query";
import { IPlayerOverallStats } from "../../../../../services/stats";

export interface IUtilityData {
  desc: string;
  value: string;
}

export interface IPlayerUtilityTableProps {
  steamid: string;
}

export interface IPlayerUtilityTableRequest {
  steamid: string;
}

export interface IPlayerUtilityTableResponse {
  state: QueryStatus;
  columns: MRT_ColumnDef<IUtilityData>[];
  data: IUtilityData[];
  maxUD: number;
  playerOverall?: IPlayerOverallStats;
}
