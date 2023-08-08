import { MRT_ColumnDef } from "material-react-table";
import { QueryStatus } from "react-query";
import { IPlayerOverallStats } from "../../../../../services/stats";

export interface IHeadShotData {
  desc: string;
  value: string;
}

export interface IPlayerHeadShotTableProps {
  steamid: string;
}

export interface IPlayerHeadShotTableRequest {
  steamid: string;
}

export interface IPlayerHeadShotTableResponse {
  state: QueryStatus;
  columns: MRT_ColumnDef<IHeadShotData>[];
  data: IHeadShotData[];
  playerOverall?: IPlayerOverallStats;
}
