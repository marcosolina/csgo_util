import { MRT_ColumnDef } from "material-react-table";
import { QueryStatus } from "react-query";
import { ITeamMatchResults } from "../../../../services/stats";

export interface ITeamMatchContentResponse {
  state: QueryStatus;
  columns: MRT_ColumnDef<ITeamMatchResults>[];
  data: ITeamMatchResults[];
  refetch: () => void;
}

export interface ITeamMatchContentRequest {
  match_id: number;
  team: string;
}
