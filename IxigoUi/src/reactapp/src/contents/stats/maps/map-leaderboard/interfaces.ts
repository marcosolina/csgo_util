import { MRT_ColumnDef } from "material-react-table";
import { QueryStatus } from "react-query";
import { IPlayerMatch } from "../../../../services/stats";

export interface IMapLeaderboardResponse {
  state: QueryStatus;
  columns: MRT_ColumnDef<IPlayerMatch>[];
  data: IPlayerMatch[];
  refetch: () => void;
}

export interface IMapLeaderboardRequest {
  mapName: string;
}