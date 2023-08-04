import { MRT_ColumnDef } from "material-react-table";
import { QueryStatus } from "react-query";
import { IPlayerClutchStats } from "../../../../../services/stats";

export interface IPlayerData {
  "1v1": string;
  "1v2": string;
  "1v3": string;
  "1v4": string;
  "1v5": string;
}

export interface IPlayerClutchTableProps {
  steamid: string;
}

export interface IPlayerClutchRequest {
  steamid: string;
}

export interface IPlayerClutchResponse {
  state: QueryStatus;
  columns: MRT_ColumnDef<IPlayerData>[];
  data: IPlayerData[];
  playerClutchStats?: IPlayerClutchStats;
}
