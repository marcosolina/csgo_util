import { MRT_ColumnDef } from "material-react-table";
import { QueryStatus } from "react-query";
import { IPlayerMatch } from "../../../services/stats";

export interface ILeaderboardContent {
  state: QueryStatus;
  columns: MRT_ColumnDef<IPlayerMatch>[];
  data: IPlayerMatch[];
  refetch: () => void;
}
