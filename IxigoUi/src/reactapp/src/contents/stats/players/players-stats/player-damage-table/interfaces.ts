import { MRT_ColumnDef } from "material-react-table";
import { QueryStatus } from "react-query";
import { IPlayerOverallStats } from "../../../../../services/stats";

export interface IDamageData {
  desc: string;
  value: string;
}

export interface IPlayerDamageTableProps {
  steamid: string;
}

export interface IPlayerDamageTableRequest {
  steamid: string;
}

export interface IPlayerDamageTableResponse {
  state: QueryStatus;
  columns: MRT_ColumnDef<IDamageData>[];
  data: IDamageData[];
  maxAdr: number;
  playerOverall?: IPlayerOverallStats;
}
