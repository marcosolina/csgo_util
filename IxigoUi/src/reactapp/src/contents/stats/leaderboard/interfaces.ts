import { MRT_ColumnDef } from "material-react-table";
import { QueryStatus } from "react-query";
import { IPlayerStats } from "../../../services/stats";

export interface ILeaderboardContent {
  state: QueryStatus;
  columns: MRT_ColumnDef<IPlayerStats>[];
  data: IPlayerStats[];
  refetch: () => void;
}
