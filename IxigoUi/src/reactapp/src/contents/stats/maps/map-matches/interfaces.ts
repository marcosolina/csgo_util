import { MRT_ColumnDef } from "material-react-table";
import { QueryStatus } from "react-query";
import { IMatchResults } from "../../../../services/stats";

export interface IMapMatchContent {
  state: QueryStatus;
  columns: MRT_ColumnDef<IMatchResults>[];
  data: IMatchResults[];
  refetch: () => void;
}

export interface IMapMatchRequest {
  mapName: string;
}
