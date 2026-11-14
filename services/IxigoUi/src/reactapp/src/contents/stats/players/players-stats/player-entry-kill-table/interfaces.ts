import { MRT_ColumnDef } from "material-react-table";
import { QueryStatus } from "react-query";
import { IPlayerEntryKillStats } from "../../../../../services/stats";

export interface IEntryKillData {
  desc: string;
  all: string;
  ts: string;
  ct: string;
}

export interface IPlayerEntryKillTableProps {
  steamid: string;
}

export interface IPlayerEntryKillRequest {
  steamid: string;
}

export interface IPlayerEntryKillResponse {
  state: QueryStatus;
  columns: MRT_ColumnDef<IEntryKillData>[];
  data: IEntryKillData[];
  entryKillStats?: IPlayerEntryKillStats;
}
