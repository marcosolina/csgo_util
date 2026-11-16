import { MRT_ColumnDef } from "material-react-table";
import { QueryStatus } from "react-query";
import { IMatchResults } from "../../../services/stats";

export interface IMatchContent {
  state: QueryStatus;
  columns: MRT_ColumnDef<IMatchResults>[];
  data: IMatchResults[];
  refetch: () => void;
}
